import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

const Editor = () => {
  const [analysis, setAnalysis] = useState("");

  useSubscription("/topic/analysis", (message) => {
    console.log("Received analysis:", JSON.parse(message.body));
    setAnalysis(JSON.parse(message.body));
  });

  if (!analysis) {
    return (
      <div className="flex items-center justify-center h-[60vh] text-muted-foreground">
        Esperando análisis de casos de uso...
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold text-foreground">Análisis de Casos de Uso</h1>
      <div className="grid gap-4">
        {analysis?.use_cases?.map((uc: { id: number; actor: string; precondition: string; trigger: string; main_flow: string }) => (
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
    </div>
  );
};

export default Editor