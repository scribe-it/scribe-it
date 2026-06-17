import { Button } from "@/components/ui/button";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { UseCase } from "./editor";
import { Card } from "@/components/ui/card";
import { XIcon } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";
import { RemoveUseCaseConfirmDialog, PublishDraftConfirmDialog } from "@/components";

type Draft = {
    id: number;
    title: string;
    content: UseCase[]
}

type ViewTransitionDocument = Document & {
    startViewTransition?: (callback: () => void | Promise<void>) => { finished: Promise<void> };
};

const getUseCaseSectionId = (draftId: number, useCaseId: number) => `draft-${draftId}-uc-${useCaseId}`;
const splitNumberedItems = (text: string) => {
    return text
        .split(/(?=\b\d+\.\s)/)
        .map((item) => item.trim())
        .filter(Boolean);
};

const Draft = () => {

    const [ showDetails, setShowDetails ] = useState(0);
    const [isRemoveDialogOpen, setIsRemoveDialogOpen] = useState(false);
    const [useCaseToRemove, setUseCaseToRemove] = useState<{ draftId: number; useCaseId: number } | null>(null);
    const [isPublishDialogOpen, setIsPublishDialogOpen] = useState(false);
    const [draftToPublish, setDraftToPublish] = useState<number | null>(null);
    const {
        data: drafts
    } = useQuery({
        queryKey: ["drafts"],
        queryFn: async () => {
            const res = await fetch("/api/v1/document/drafts", {
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            if(!res.ok) throw new Error("Error obteniendo borradores");
            return res.json();
        }
    })

    const handleToggleDetails = (id: number) => {
        const nextId = showDetails === id ? 0 : id;
        const doc = document as ViewTransitionDocument;

        if (doc.startViewTransition) {
            doc.startViewTransition(() => {
                setShowDetails(nextId);
            });
            return;
        }

        setShowDetails(nextId);
    };

    const scrollToUseCase = (draftId: number, useCaseId: number) => {
        const sectionId = getUseCaseSectionId(draftId, useCaseId);
        const section = document.getElementById(sectionId);
        section?.scrollIntoView({ behavior: "smooth", block: "start" });
    };

    const queryClient = useQueryClient();
    const {
        mutateAsync: removeUseCase,
        isPending: isRemovingUseCase
    } = useMutation({
        mutationFn: async ({ draftId, useCaseId }: { draftId: number; useCaseId: number }) => {
            const res = await fetch(`/api/v1/document/${draftId}/remove/${useCaseId}`, {
                method: "PATCH",
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            if (!res.ok) throw new Error("Error eliminando caso de uso");
        },
        onSuccess: () => {
            toast.success("Caso de uso eliminado exitosamente");
            setUseCaseToRemove(null);
            setIsRemoveDialogOpen(false);
            queryClient.invalidateQueries({ queryKey: ["drafts"] });
        },
        onError: () => {
            toast.error("No se pudo eliminar el caso de uso. Intenta nuevamente.");
        }
    })


    const {
        mutateAsync: publishDraft,
        isPending: isPublishingDraft
    } = useMutation({
        mutationFn: async (draftId: number) => {
            const res = await fetch(`/api/v1/document/${draftId}/publish`, {
                method: "POST",
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            if (!res.ok) {
                throw new Error("Error publicando borrador");
            }
        },
        onSuccess: () => {
            toast.success("Borrador publicado exitosamente");
            queryClient.invalidateQueries({ queryKey: ["drafts"] });
        },
        onError: () => {
            toast.error("No se pudo publicar el borrador. Intenta nuevamente.");
        }
    })

    const handleRemoveUseCase = async (draftId: number, useCaseId: number) => {
        setUseCaseToRemove({ draftId, useCaseId });
        setIsRemoveDialogOpen(true);
    };

    const handleConfirmRemoveUseCase = async () => {
        if (!useCaseToRemove) return;

        await removeUseCase({
            draftId: useCaseToRemove.draftId,
            useCaseId: useCaseToRemove.useCaseId,
        });
    };

    const handleRequestPublish = (draftId: number) => {
        setDraftToPublish(draftId);
        setIsPublishDialogOpen(true);
    };

    const handleConfirmPublish = async () => {
        if (!draftToPublish) return;
        await publishDraft(draftToPublish);
        setIsPublishDialogOpen(false);
        setDraftToPublish(null);
    };

  return (
    <div className="flex h-[100vh] w-full flex-col gap-4 bg-background p-4 text-white">
        <RemoveUseCaseConfirmDialog
            open={isRemoveDialogOpen}
            onOpenChange={(open) => {
                setIsRemoveDialogOpen(open);
                if (!open) setUseCaseToRemove(null);
            }}
            onCancel={() => {
                setIsRemoveDialogOpen(false);
                setUseCaseToRemove(null);
            }}
            onConfirm={handleConfirmRemoveUseCase}
            isPending={isRemovingUseCase}
        />
        <PublishDraftConfirmDialog
            open={isPublishDialogOpen}
            onOpenChange={(open) => {
                setIsPublishDialogOpen(open);
                if (!open) setDraftToPublish(null);
            }}
            onCancel={() => {
                setIsPublishDialogOpen(false);
                setDraftToPublish(null);
            }}
            onConfirm={handleConfirmPublish}
            isPending={isPublishingDraft}
        />
        <h1 className="text-2xl font-bold text-foreground w-full">Borradores</h1>
        {
            drafts?.length ? (
                <ul className={showDetails ? "relative flex-1 min-h-0" : "grid flex-1 auto-rows-max grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4"}>
                    {drafts.map((d: Draft) => (
                        <Card
                            key={d.id}
                            className={
                                showDetails === d.id
                                    ? "absolute inset-0 z-50 m-0 overflow-y-auto rounded-lg border border-border bg-card p-6"
                                    : showDetails
                                        ? "hidden"
                                        : "relative cursor-pointer rounded-lg border border-border p-4 transition-colors hover:bg-accent"
                            }
                        >
                            {showDetails === d.id && (
                                <XIcon
                                    className="absolute top-4 right-4 cursor-pointer rounded-lg hover:bg-red-500"
                                    onClick={() => handleToggleDetails(d.id)}
                                />
                            )}
                            <p>Título: {d.title}</p>
                            <div>
                                <hr/>
                                {
                                    showDetails === d.id && d.content.length > 0 && (
                                        <nav className="sticky top-0 z-10 mt-4 rounded-lg border border-border bg-background/90 p-3 backdrop-blur">
                                            <p className="mb-2 text-sm font-semibold text-foreground">Indice de casos de uso</p>
                                            <div className="flex flex-wrap gap-2">
                                                {d.content.map((cu: UseCase) => (
                                                    <Button
                                                        key={`index-${cu.id}`}
                                                        type="button"
                                                        variant="secondary"
                                                        size="sm"
                                                        onClick={() => scrollToUseCase(d.id, cu.id)}
                                                    >
                                                        Caso #{cu.id}
                                                    </Button>
                                                ))}
                                            </div>
                                        </nav>
                                    )
                                }
                                {
                                    showDetails === d.id && (
                                    d.content.map((cu: UseCase) => (
                                        (() => {
                                            const mainFlowItems = splitNumberedItems(cu.main_flow);
                                            const hasNumberedSteps = mainFlowItems.length > 1;

                                            return (
                                        <div id={getUseCaseSectionId(d.id, cu.id)} key={cu.id} className="mt-4 scroll-mt-24 rounded-lg border border-border flex flex-col gap-4 p-4 relative">
                                            <XIcon className="absolute top-4 right-4 cursor-pointer rounded-lg hover:bg-red-500" onClick={() => handleRemoveUseCase(d.id, cu.id)} />
                                            <h1><span className="font-bold text-lg">Caso de uso N°: </span>{cu.id}</h1>
                                            <h2><span className="font-bold text-lg">Actor: </span>{cu.actor}</h2>
                                            <div className="space-y-2">
                                                <h4><span className="font-bold text-lg">Flujo principal:</span></h4>
                                                {hasNumberedSteps ? (
                                                    <ol className="list-decimal space-y-1 pl-6">
                                                        {mainFlowItems.map((step, index) => (
                                                            <li key={`${cu.id}-step-${index}`}>
                                                                {step.replace(/^\d+\.\s*/, "")}
                                                            </li>
                                                        ))}
                                                    </ol>
                                                ) : (
                                                    <p>{cu.main_flow}</p>
                                                )}
                                            </div>
                                            <Button className="self-end">Editar</Button>
                                        </div>
                                            );
                                        })()
                                    ))
                                    )
                                }
                            </div>
                            <div className="flex items-center justify-center gap-2">
                                <Button onClick={() => handleRequestPublish(d.id)}>Publicar</Button>
                                <Button onClick={() => handleToggleDetails(d.id)}>{showDetails === d.id ? "Ocultar detalle" : "Ver detalle"}</Button>
                            </div>
                        </Card>
                    ))}
                </ul>
            ) : (
                <p>No hay borradores guardados.</p>
            )
        }
    </div>
  )
}

export default Draft