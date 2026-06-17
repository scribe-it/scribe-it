import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";
import { MessageSquare, Send } from "lucide-react";
import type { UseCase } from "./editor";

type PublishedDocument = {
  id: number;
  title: string;
  content: UseCase[];
};

type Comment = {
  id: number;
  text: string;
  authorName: string;
  createdAt: string;
};

const splitNumberedItems = (text: string) => {
  return text
    .split(/(?=\b\d+\.\s)/)
    .map((item) => item.trim())
    .filter(Boolean);
};

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr);
  return date.toLocaleString("es-ES", {
    day: "numeric",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const CommentsSection = ({ documentId }: { documentId: number }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [newComment, setNewComment] = useState("");

  const queryClient = useQueryClient();

  const {
    data: comments,
    isLoading,
  } = useQuery<Comment[]>({
    queryKey: ["comments", documentId],
    queryFn: async () => {
      const res = await fetch(`/api/v1/document/${documentId}/comments`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      if (!res.ok) throw new Error("Error obteniendo comentarios");
      return res.json();
    },
    enabled: isOpen,
  });

  const {
    mutateAsync: addComment,
    isPending: isAdding,
  } = useMutation({
    mutationFn: async (text: string) => {
      const res = await fetch(`/api/v1/document/${documentId}/comments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({ text }),
      });
      if (!res.ok) throw new Error("Error agregando comentario");
      return res.json();
    },
    onSuccess: () => {
      toast.success("Comentario agregado");
      setNewComment("");
      queryClient.invalidateQueries({ queryKey: ["comments", documentId] });
    },
    onError: () => {
      toast.error("No se pudo agregar el comentario");
    },
  });

  const handleSubmit = async () => {
    if (!newComment.trim()) return;
    await addComment(newComment.trim());
  };

  return (
    <div className="mt-4 border-t border-border pt-3">
      <Button
        variant="ghost"
        size="sm"
        className="flex items-center gap-2 text-muted-foreground"
        onClick={() => setIsOpen(!isOpen)}
      >
        <MessageSquare className="size-4" />
        {isOpen ? "Ocultar comentarios" : `Comentarios (${comments?.length ?? 0})`}
      </Button>

      {isOpen && (
        <div className="mt-3 space-y-3">
          {isLoading && (
            <p className="text-sm text-muted-foreground">Cargando comentarios...</p>
          )}

          {!isLoading && (!comments || comments.length === 0) && (
            <p className="text-sm text-muted-foreground">Sin comentarios aún.</p>
          )}

          {!isLoading && comments && comments.length > 0 && (
            <div className="space-y-2 max-h-60 overflow-y-auto">
              {comments.map((comment) => (
                <div
                  key={comment.id}
                  className="rounded-md border border-border p-2 text-sm"
                >
                  <div className="flex items-center justify-between gap-2 mb-1">
                    <span className="font-medium text-foreground">
                      {comment.authorName}
                    </span>
                    <span className="text-xs text-muted-foreground">
                      {formatDate(comment.createdAt)}
                    </span>
                  </div>
                  <p className="text-muted-foreground">{comment.text}</p>
                </div>
              ))}
            </div>
          )}

          <div className="flex gap-2">
            <Input
              placeholder="Escribe un comentario..."
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter" && !e.shiftKey) {
                  e.preventDefault();
                  handleSubmit();
                }
              }}
              disabled={isAdding}
            />
            <Button
              size="icon"
              onClick={handleSubmit}
              disabled={isAdding || !newComment.trim()}
            >
              <Send className="size-4" />
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

const Publications = () => {
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

  return (
    <div className="p-6 space-y-6 bg-background w-full min-h-screen">
      <h1 className="text-2xl font-bold text-foreground w-full">Publicaciones</h1>

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
          {publications.map((publication) => (
            <Card key={publication.id} className="rounded-lg border border-border">
              <CardHeader>
                <div className="flex items-center justify-between gap-2">
                  <CardTitle className="text-lg">{publication.title}</CardTitle>
                  <Badge variant="secondary">#{publication.id}</Badge>
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
    </div>
  );
};

export default Publications;
