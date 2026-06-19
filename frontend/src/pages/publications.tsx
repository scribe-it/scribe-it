import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { FileText } from "lucide-react";
import html2canvas from "html2canvas";
import { jsPDF } from "jspdf";
import type { UseCase } from "./editor";
import { CommentsSection } from "./comments-section";
import { usePagination } from "@/hooks/use-pagination";
import { DataPagination } from "@/components/data-pagination";
import { useAuth } from "@/context/use-auth";

type PublishedDocument = {
  id: number;
  title: string;
  content: UseCase[];
};

const splitNumberedItems = (text: string) => {
  return text
    .split(/(?=\b\d+\.\s)/)
    .map((item) => item.trim())
    .filter(Boolean);
};

const Publications = () => {
  const queryClient = useQueryClient();
  const { userData } = useAuth();

  const {
    data: publications,
    isLoading,
    isError,
  } = useQuery<PublishedDocument[]>({
    queryKey: ["published-documents"],
    queryFn: async () => {
      const res = await fetch("/api/v1/document/published", {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });

      if (!res.ok) {
        throw new Error("Error obteniendo publicaciones");
      }

      return res.json();
    },
  });

  const { page, setPage, totalPages, paginatedItems: paginatedPublications } = usePagination(publications, 6);

  const {
    mutateAsync: unpublishPublication,
    isPending: isUnpublishing,
  } = useMutation({
    mutationFn: async (documentId: number) => {
      const res = await fetch(`/api/v1/document/${documentId}/unpublish`, {
        method: "PATCH",
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      if (!res.ok) throw new Error("Error despublicando");
    },
    onSuccess: () => {
      toast.success("Publicación revertida a borrador");
      queryClient.invalidateQueries({ queryKey: ["published-documents"] });
      queryClient.invalidateQueries({ queryKey: ["drafts"] });
    },
    onError: () => {
      toast.error("No se pudo despublicar. Intenta nuevamente.");
    },
  });

  const exportPDF = async (publication: PublishedDocument) => {
    const loading = toast.loading("Generando PDF...");

    const content = document.createElement("div");
    content.style.cssText = "padding: 2rem; font-family: sans-serif; color: #000;";
    content.innerHTML = `
      <h1 style="font-size: 1.5rem; margin-bottom: 0.5rem;">${publication.title}</h1>
      <hr style="margin-bottom: 1.5rem; border-color: #ccc;" />
      ${publication.content.map((uc) => {
        const flowSteps = splitNumberedItems(uc.main_flow);
        const hasNumberedSteps = flowSteps.length > 1;
        return `
          <div style="margin-bottom: 1.5rem; border: 1px solid #ddd; border-radius: 8px; padding: 1rem;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
              <strong>Caso #${uc.id}</strong>
              <span style="font-size: 0.875rem; color: #666;">${uc.actor}</span>
            </div>
            <p style="font-size: 0.75rem; color: #888; margin-bottom: 0.25rem;">Flujo principal</p>
            ${hasNumberedSteps ? `<ol style="margin: 0; padding-left: 1.25rem;">${flowSteps.map(s => `<li style="font-size: 0.875rem;">${s.replace(/^\d+\.\s*/, "")}</li>`).join("")}</ol>` : `<p style="font-size: 0.875rem;">${uc.main_flow}</p>`}
          </div>
        `;
      }).join("")}
    `;
    document.body.appendChild(content);

    const canvas = await html2canvas(content, { scale: 2 });
    document.body.removeChild(content);

    const imgData = canvas.toDataURL("image/png");
    const pdf = new jsPDF("p", "mm", "a4");
    const imgWidth = 190;
    const imgHeight = (canvas.height * imgWidth) / canvas.width;
    let heightLeft = imgHeight;
    let position = 0;

    pdf.addImage(imgData, "PNG", 10, position, imgWidth, imgHeight);
    heightLeft -= pdf.internal.pageSize.getHeight() - 20;

    while (heightLeft > 0) {
      position = heightLeft - imgHeight;
      pdf.addPage();
      pdf.addImage(imgData, "PNG", 10, position, imgWidth, imgHeight);
      heightLeft -= pdf.internal.pageSize.getHeight() - 20;
    }

    pdf.save(`${publication.title}.pdf`);
    toast.dismiss(loading);
    toast.success("PDF exportado exitosamente");
  };

  return (
    <div className="p-6 space-y-6 bg-background w-full min-h-screen ">
      <h1 className="text-2xl font-bold text-foreground w-full border-b border-white/[0.07] pb-4">Publicaciones</h1>

      {isLoading && (
        <p className="text-muted-foreground">Cargando publicaciones...</p>
      )}

      {isError && (
        <p className="text-red-400">No se pudieron cargar las publicaciones.</p>
      )}

      {!isLoading && !isError && publications?.length === 0 && (
        <p className="text-muted-foreground">No hay publicaciones disponibles.</p>
      )}

      {!isLoading && !isError && !!publications?.length && (
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {paginatedPublications.map((publication) => (
            <Card key={publication.id} className="relative rounded-lg border border-border">
              <CardHeader>
                <div className="flex items-center justify-between gap-2">
                  <CardTitle className="text-lg">{publication.title}</CardTitle>
                  <div className="flex items-center gap-2">
                    {userData?.role === "ANALISTA" && (
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => unpublishPublication(publication.id)}
                        disabled={isUnpublishing}
                      >
                        Despublicar
                      </Button>
                    )}
                    <Button
                      variant="ghost"
                      size="icon"
                      className="size-8"
                      onClick={() => exportPDF(publication)}
                    >
                      <FileText className="size-4" />
                    </Button>
                    <Badge variant="secondary">#{publication.id}</Badge>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {publication.content?.length ? (
                  publication.content.map((useCase) => {
                    const flowSteps = splitNumberedItems(useCase.main_flow);
                    const hasNumberedSteps = flowSteps.length > 1;

                    return (
                      <div
                        key={`${publication.id}-${useCase.id}`}
                        className="rounded-md border border-border p-3 space-y-2"
                      >
                        <div className="flex items-center justify-between gap-2">
                          <p className="font-medium text-foreground">Caso #{useCase.id}</p>
                          <Badge variant="outline">{useCase.actor}</Badge>
                        </div>
                        <div className="space-y-1">
                          <p className="text-xs text-muted-foreground">Flujo principal</p>
                          {hasNumberedSteps ? (
                            <ol className="list-decimal pl-5 text-sm space-y-1">
                              {flowSteps.map((step, index) => (
                                <li key={`${useCase.id}-published-step-${index}`}>
                                  {step.replace(/^\d+\.\s*/, "")}
                                </li>
                              ))}
                            </ol>
                          ) : (
                            <p className="text-sm">{useCase.main_flow}</p>
                          )}
                        </div>
                      </div>
                    );
                  })
                ) : (
                  <p className="text-sm text-muted-foreground">Sin casos de uso.</p>
                )}

                <CommentsSection documentId={publication.id} />
              </CardContent>
            </Card>
          ))}
        </div>
      )}
      <DataPagination page={page} totalPages={totalPages} onPageChange={setPage} />
    </div>
  );
};

export default Publications;
