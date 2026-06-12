import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { useQuery } from "@tanstack/react-query";

type UseCase = {
  id: number;
  actor: string;
  precondition: string;
  trigger: string;
  main_flow: string;
}

type Analysis = {
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
    data
  } = useQuery({
    queryKey: ["use_cases"],
    queryFn: async () => {
      const res = await fetch("/api/v1/use_cases", {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      return res.json();
    }
  })
  if (!analysis) {
    return (
      <div className="flex items-center justify-center h-[60vh] text-muted-foreground bg-background w-full h-screen">
        Esperando análisis de casos de uso...
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6 flex">
      <h1 className="text-2xl font-bold text-foreground">Análisis de Casos de Uso</h1>
      <div className="grid gap-4">
        {analysis?.use_cases?.map((uc: UseCase) => (
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
                <p className="text-sm">{uc.precondition}</p>
              </div>
              <div>
                <span className="text-xs font-medium text-muted-foreground">Trigger</span>
                <p className="text-sm">{uc.trigger}</p>
              </div>
              <div>
                <span className="text-xs font-medium text-muted-foreground">Flujo Principal</span>
                <p className="text-sm">{uc.main_flow}</p>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
      <aside className="w-56 m-10">
        <div className="flex flex-col gap-4">
          <h2>Historial de casos de uso</h2>
          {
            data?.map((uc: UseCase) => (
              <Card key={uc.id} className="p-2" onClick={() => setAnalysis({ use_cases: [uc] })}>
                <CardContent className="flex items-center justify-between">
                  <span className="text-sm">Caso de Uso #{uc.id}</span>
                  <Badge variant="secondary">{uc.actor}</Badge>
                </CardContent>
              </Card>
            ))
          }
        </div>
      </aside>
    </div>
  );
};

export default Editor