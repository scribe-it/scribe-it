import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Save } from "lucide-react";
import { toast } from "sonner";
import AddToDerDialog from "@/components/add-to-der-dialog";
import EditorSidebar from "@/components/editor-sidebar";

export type UseCase = {
  id: number;
  actor: string;
  precondition: string;
  trigger: string;
  main_flow: string;
  postcondition: string;
  history?: { messages: string };
}

export type Analysis = {
  use_cases: UseCase[];
}

const Editor = () => {
  const [analysis, setAnalysis] = useState<Analysis>({
    use_cases: [],
  });

  useSubscription("/topic/analysis", (message) => {
    console.log("Received analysis:", JSON.parse(message.body));
    setAnalysis(JSON.parse(message.body));
  });

  const {
    data:use_cases
  } = useQuery({
    queryKey: ["use_cases"],
    queryFn: async () => {
      const res = await fetch("/api/v1/use_cases", {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      return res.json();
    }
  })

  // const {
  //   data:analysis_messages
  // } = useQuery({
  //   queryKey: ["analysis_messages"],
  //   queryFn: async () => {
  //     const res = await fetch(`/api/v1/admin/use-case/${analysis.use_cases[0].id}/messages`, {
  //       headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
  //     });
  //     return res.json();
  //   },
  //   enabled: !!analysis.use_cases.length
  // })

  const queryClient = useQueryClient();

  const {
    mutateAsync: deleteUseCase
  } = useMutation({
    mutationFn: async (id: number) => {
      const res = await fetch(`/api/v1/use_cases/${id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      if(!res.ok) {
        throw new Error("Error al eliminar el caso de uso");
      }
    },
    onError: () => {
      toast.error("Error al eliminar el caso de uso");
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["use_cases"] });
      toast.success("Caso de uso eliminado exitosamente");
    }
  });

  const handleDeleteUseCase = async (id: number) => {
    await deleteUseCase(id);
  }

  const { mutateAsync: updateUseCase, isPending: isUpdating } = useMutation({
    mutationFn: async (uc: UseCase) => {
      const res = await fetch(`/api/v1/use_cases/${uc.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(uc),
      });
      if (!res.ok) throw new Error("Error al actualizar");
      return res.json();
    },
    onError: () => toast.error("Error al actualizar el caso de uso"),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["use_cases"] });
      toast.success("Caso de uso actualizado");
    },
  });

  const handleFieldChange = (index: number, field: keyof UseCase, value: string) => {
    setAnalysis(prev => {
      const updated = [...prev.use_cases];
      updated[index] = { ...updated[index], [field]: value };
      return { use_cases: updated };
    });
  };

  if (!use_cases) {
    return (
      <div className="flex items-center justify-center h-[60vh] text-white bg-background w-full h-screen">
        No se han encontrado casos de uso. Por favor, ingresa algunos mensajes en el chat para generar casos de uso.
      </div>
    );
  }

  return (
    <div className="flex w-full bg-background">
      <main className="flex-1 space-y-6 p-6">
        <h1 className="text-2xl font-bold text-foreground w-full">Análisis de Casos de Uso</h1>
        {
          !analysis?.use_cases?.length && (
            <div className="flex justify-center h-[60vh] pt-72 text-lg text-white bg-background w-full h-screen">
              Seleccione un caso de uso de la derecha para ver su análisis detallado.
            </div>
          )
        }
        <div className="w-full grid md:grid-cols-2 gap-6">
          <div className="grid gap-4">
            {analysis?.use_cases?.map((uc: UseCase, index) => (
              <Card key={uc.id}>
                <CardHeader>
                  <div className="flex items-center gap-2">
                    <CardTitle className="text-lg">Caso de Uso #{uc.id}</CardTitle>
                    <Badge variant="secondary">{uc.actor}</Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-3">
                  <div>
                    <span className="text-xs font-medium text-muted-foreground">Precondición</span>
                    <textarea
                      className="w-full text-sm bg-transparent border border-border rounded-md p-2 text-foreground resize-y mt-1"
                      rows={2}
                      value={uc.precondition}
                      onChange={(e) => handleFieldChange(index, "precondition", e.target.value)}
                    />
                  </div>
                  <div>
                    <span className="text-xs font-medium text-muted-foreground">Trigger</span>
                    <textarea
                      className="w-full text-sm bg-transparent border border-border rounded-md p-2 text-foreground resize-y mt-1"
                      rows={2}
                      value={uc.trigger}
                      onChange={(e) => handleFieldChange(index, "trigger", e.target.value)}
                    />
                  </div>
                  <div>
                    <span className="text-xs font-medium text-muted-foreground">Flujo Principal</span>
                    <textarea
                      className="w-full text-sm bg-transparent border border-border rounded-md p-2 text-foreground resize-y mt-1"
                      rows={3}
                      value={uc.main_flow}
                      onChange={(e) => handleFieldChange(index, "main_flow", e.target.value)}
                    />
                  </div>
                  <div>
                    <span className="text-xs font-medium text-muted-foreground">Postcondición</span>
                    <textarea
                      className="w-full text-sm bg-transparent border border-border rounded-md p-2 text-foreground resize-y mt-1"
                      rows={2}
                      value={uc.postcondition}
                      onChange={(e) => handleFieldChange(index, "postcondition", e.target.value)}
                    />
                  </div>
                  <div className="flex gap-2 justify-between">
                    <Button size="sm" onClick={() => updateUseCase(uc)} disabled={isUpdating}>
                      <Save className="size-4 mr-1" />
                      Guardar
                    </Button>
                    <AddToDerDialog useCase={uc} />
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
          <div>
            <h2 className="text-xl font-semibold text-white mb-4">Mensajes originales</h2>
            {
              analysis?.use_cases?.[0]?.history?.messages && 
                <Card className="mb-2">
                  <CardContent>
                    <p className="text-sm">{analysis?.use_cases?.[0]?.history?.messages}</p>
                  </CardContent>
                </Card>
            }
          </div>
        </div>
      </main>
      <EditorSidebar useCases={use_cases} onDelete={handleDeleteUseCase} setAnalysis={setAnalysis} />
    </div>
  );
};

export default Editor