import { useState } from "react"
import { PlusIcon, ArrowLeft, Loader2 } from "lucide-react"
import { Button } from "./ui/button"
import { Input } from "./ui/input"
import { Dialog, DialogContent, DialogHeader, DialogFooter, DialogTitle, DialogTrigger, DialogDescription, } from "./ui/dialog"
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query"
import { UseCase } from "@/pages/editor"
import { toast } from "sonner"

export interface IDocument {
    id: number;
    title: string;
    content: UseCase[];
}

const AddToDerDialog = ({ useCase }: { useCase: UseCase }) => {
    const [open, setOpen] = useState(false)
    const [view, setView] = useState<"list" | "create">("list")
    const [newTitle, setNewTitle] = useState("")
    const queryClient = useQueryClient()

    const {
        data: documents
    } = useQuery({
        queryKey: ["documents"],
        queryFn: async () => {
            const res = await fetch("/api/v1/document/drafts", {
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            if(!res.ok) throw new Error("Error obteniendo documentos");
            return res.json();
        }
    })

    const { mutateAsync: createDocument, isPending } = useMutation({
        mutationFn: async (title: string) => {
            const res = await fetch("/api/v1/document", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify({ title, content: [useCase] }),
            })
            if (!res.ok) throw new Error("Error creando documento")
            return res.json()
        },
        onError: () => toast.error("Error al crear el documento"),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["documents"] })
            setNewTitle("")
            setView("list")
            setOpen(false)
            toast.success("Documento creado exitosamente")
        }
    })

    const handleCreate = () => {
        if (newTitle.trim()) createDocument(newTitle.trim())
    }

    const handleUpdate = async (docId: number) => {
        const doc = documents?.find((d: IDocument) => d.id === docId);
        if (!doc) return;
        const res = await fetch(`/api/v1/document/${docId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            body: JSON.stringify(useCase),
        });
        if (!res.ok) {
            toast.error("Error al actualizar el documento");
            return;
        }
        
        queryClient.invalidateQueries({ queryKey: ["documents"] });
        toast.success("Documento actualizado exitosamente");
        setOpen(false)
    };

    const resetView = () => {
        setView("list")
        setNewTitle("")
    }

    return (
        <Dialog open={open} onOpenChange={(nextOpen) => {
            setOpen(nextOpen)
            if (!nextOpen) resetView()
        }}>
            <DialogTrigger asChild>
             <Button size="sm">
                <PlusIcon className="size-4 mr-1" />
                    Agregar a DER
                </Button>
            </DialogTrigger>
            <DialogContent>
                {view === "list" ? (
                    <>
                        <DialogHeader>
                            <DialogTitle>Agregar a DER</DialogTitle>
                            <DialogDescription>
                                Selecciona un documento de especificación de requisitos (DER) al cual agregarle el caso de uso o crea uno nuevo.
                            </DialogDescription>
                        </DialogHeader>
                        {documents?.length ? (
                            <div className="space-y-2 max-h-60 overflow-y-auto space-x-2">
                                {documents.map((doc: IDocument) => (
                                    <Button key={doc.id} 
                                        onClick={() => handleUpdate(doc.id)}
                                        className="p-3 border bg-background border-border rounded-lg hover:bg-accent cursor-pointer transition-colors">
                                        <h4 className="font-semibold text-sm">{doc.title}</h4>
                                    </Button>
                                ))}
                            </div>
                        ) : (
                            <p className="text-sm text-muted-foreground text-center py-4">
                                No hay documentos disponibles.
                            </p>
                        )}
                        <Button onClick={() => setView("create")}>
                            <PlusIcon className="size-4 mr-1" />
                            Crear nuevo documento
                        </Button>
                        <DialogFooter>
                            <Button variant="outline" onClick={() => setOpen(false)}>Cerrar</Button>
                        </DialogFooter>
                    </>
                ) : (
                    <>
                        <DialogHeader>
                            <DialogTitle>Nuevo documento</DialogTitle>
                            <DialogDescription>
                                Ingresa el título del nuevo documento de especificación de requisitos.
                            </DialogDescription>
                        </DialogHeader>
                        <form onSubmit={(e) => { e.preventDefault(); handleCreate() }} className="space-y-4">
                            <Input
                                placeholder="Título del documento"
                                value={newTitle}
                                onChange={(e) => setNewTitle(e.target.value)}
                                autoFocus
                            />
                            <DialogFooter className="flex justify-between">
                                <Button type="button" variant="ghost" onClick={resetView} disabled={isPending}>
                                    <ArrowLeft className="size-4 mr-1" />
                                    Volver
                                </Button>
                                <Button type="submit" disabled={!newTitle.trim() || isPending}>
                                    {isPending && <Loader2 className="size-4 mr-1 animate-spin" />}
                                    {isPending ? "Creando..." : "Crear"}
                                </Button>
                            </DialogFooter>
                        </form>
                    </>
                )}
            </DialogContent>
        </Dialog>
    )
}

export default AddToDerDialog