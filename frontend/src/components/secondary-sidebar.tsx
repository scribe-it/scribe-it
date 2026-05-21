import { cn } from "@/lib/utils"
import { Card } from "./ui/card"
const departments = [
    { id: 1, name: "Recursos Humanos", code: "HR" },
    { id: 2, name: "Tecnología", code: "IT" },
    { id: 3, name: "Finanzas", code: "FIN" },
    { id: 4, name: "Marketing", code: "MKT" },
    { id: 5, name: "Ventas", code: "SAL" },
    { id: 6, name: "Operaciones", code: "OPS" },
    { id: 7, name: "Atención al Cliente", code: "CS" },
    { id: 8, name: "Logística", code: "LOG" },
    { id: 9, name: "Legal", code: "LEG" },
    { id: 10, name: "Innovación", code: "R&D" },
    { id: 11, name: "Docubot", code: "IA"}
  ]
export const SecondarySidebar = () => {
    return (
        <aside className="px-20 py-10">
            <header className="mb-2">Departamentos</header>
            <div className="flex flex-col gap-4">
            {
                departments.map(u => {
                    return (
                    <Card className={cn("p-2", u.code === "IA" && "bg-blue-400 text-white")}>
                        {u.name} - {u.code}
                    </Card>
                    )
                })
            }
            </div>
        </aside>
    )
}