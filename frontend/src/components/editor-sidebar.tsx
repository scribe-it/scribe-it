import { XIcon } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Analysis, UseCase } from "@/pages/editor";
    
const EditorSidebar = ({ useCases, onDelete, setAnalysis }: { useCases: UseCase[]; onDelete: (id: number) => void; setAnalysis:  React.Dispatch<React.SetStateAction<Analysis>> }) => {
  return (
    <aside className="w-72 bg-[#1e1e1c] border-l border-white/[0.07] p-10">
        <div className="flex flex-col gap-4">
          <h2 className="text-xl font-semibold text-white mb-4 text-center">Historial de casos de uso</h2>
          {
            useCases?.map((uc: UseCase) => (
              <Card key={uc.id} className="p-2" onClick={() => setAnalysis({ use_cases: [uc] })}>
                <CardContent className="flex items-center justify-between relative ">
                  <span className="text-sm">Caso de Uso #{uc.id}</span>
                  <Badge variant="secondary">{uc.actor}</Badge>
                  <XIcon onClick={() => onDelete(uc.id)} className="absolute top-0 right-0 size-5 text-white p-1 bg-red-500 rounded-full text-muted-foreground" />
                </CardContent>
              </Card>
            ))
          }
          {
            useCases.length === 0 && (
              <div className="flex items-center justify-center h-32 text-sm text-white bg-background w-full">
                No se han generado casos de uso aún. Interactúa con el chat para generar casos de uso.
              </div>
            )
          }
        </div>
      </aside>
  );
};

export default EditorSidebar