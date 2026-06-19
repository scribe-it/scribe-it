import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { UseCase } from "./editor";
import { Search, XIcon } from "lucide-react";
import { useState, useMemo } from "react";
import { Input } from "@/components/ui/input";
import { usePagination } from "@/hooks/use-pagination";
import { DataPagination } from "@/components/data-pagination";
import { toast } from "sonner";
import { RemoveUseCaseConfirmDialog, PublishDraftConfirmDialog } from "@/components";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";

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
    const [showDetails, setShowDetails] = useState(0);
    const [isRemoveDialogOpen, setIsRemoveDialogOpen] = useState(false);
    const [useCaseToRemove, setUseCaseToRemove] = useState<{ draftId: number; useCaseId: number } | null>(null);
    const [isPublishDialogOpen, setIsPublishDialogOpen] = useState(false);
    const [draftToPublish, setDraftToPublish] = useState<number | null>(null);
    const [isDeleteDraftDialogOpen, setIsDeleteDraftDialogOpen] = useState(false);
    const [draftToDelete, setDraftToDelete] = useState<number | null>(null);
    const [searchQuery, setSearchQuery] = useState("");

    const {
        data: drafts
    } = useQuery({
        queryKey: ["drafts"],
        queryFn: async () => {
            const res = await fetch("/api/v1/document/drafts", {
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            if (!res.ok) throw new Error("Error obteniendo borradores");
            return res.json();
        }
    })

    const filteredDrafts = useMemo(
        () => (drafts as Draft[] | undefined)?.filter((d) => d.title.toLowerCase().includes(searchQuery.toLowerCase())),
        [drafts, searchQuery]
    );

    const { page, setPage, totalPages, paginatedItems: paginatedDrafts } = usePagination(filteredDrafts, 8);

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

    const {
        mutateAsync: deleteDraft,
        isPending: isDeletingDraft,
    } = useMutation({
        mutationFn: async (draftId: number) => {
            const res = await fetch(`/api/v1/document/${draftId}`, {
                method: "DELETE",
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            if (!res.ok) {
                throw new Error("Error eliminando borrador");
            }
        },
        onSuccess: () => {
            toast.success("Borrador eliminado exitosamente");
            setIsDeleteDraftDialogOpen(false);
            setDraftToDelete(null);
            queryClient.invalidateQueries({ queryKey: ["drafts"] });
        },
        onError: () => {
            toast.error("No se pudo eliminar el borrador. Intenta nuevamente.");
        },
    });

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

    const handleRequestDeleteDraft = (draftId: number) => {
        setDraftToDelete(draftId);
        setIsDeleteDraftDialogOpen(true);
    };

    const handleConfirmDeleteDraft = async () => {
        if (!draftToDelete) return;
        await deleteDraft(draftToDelete);
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
            <Dialog open={isDeleteDraftDialogOpen} onOpenChange={setIsDeleteDraftDialogOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Eliminar borrador</DialogTitle>
                        <DialogDescription>
                            Esta accion eliminara el borrador completo. Esta accion no se puede deshacer.
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button
                            type="button"
                            variant="outline"
                            onClick={() => {
                                setIsDeleteDraftDialogOpen(false);
                                setDraftToDelete(null);
                            }}
                            disabled={isDeletingDraft}
                        >
                            Cancelar
                        </Button>
                        <Button
                            type="button"
                            variant="destructive"
                            onClick={handleConfirmDeleteDraft}
                            disabled={isDeletingDraft}
                        >
                            {isDeletingDraft ? "Eliminando..." : "Eliminar"}
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>

            <div className="flex items-center gap-4 w-full">
                <h1 className="text-2xl font-bold text-foreground">Borradores</h1>
                <div className="relative ml-auto max-w-xl border border-white/[0.07] rounded-md">
                    <Search className="absolute left-2.5 top-2.5 size-4 text-muted-foreground" />
                    <Input
                        placeholder="Buscar por título..."
                        value={searchQuery}
                        onChange={(e) => { setSearchQuery(e.target.value); setPage(1); }}
                        className="pl-8"
                    />
                </div>
            </div>
            {filteredDrafts?.length ? (
                <div className="grid flex-1 auto-rows-max grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
                    {(showDetails ? drafts : paginatedDrafts).map((d: Draft) => (
                        showDetails === d.id ? (
                            <Card
                                key={d.id}
                                className="col-span-full max-h-[calc(100vh-8rem)] overflow-y-auto rounded-lg border border-border bg-card p-6"
                            >
                                <div className="flex items-center justify-between mb-4">
                                    <h2 className="text-xl font-bold text-foreground">{d.title}</h2>
                                    <div className="flex items-center gap-2">
                                        <Button onClick={() => handleRequestPublish(d.id)}>Publicar</Button>
                                        <Button variant="outline" onClick={() => handleToggleDetails(d.id)}>
                                            Cerrar detalle
                                        </Button>
                                    </div>
                                </div>
                                {d.content.length > 0 && (
                                    <nav className="sticky top-0 z-10 mb-4 rounded-lg border border-border bg-background/90 p-3 backdrop-blur">
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
                                )}
                                {d.content.map((cu: UseCase) => {
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
                                })}
                            </Card>
                        ) : (
                            <Card key={d.id} className="relative rounded-lg border border-border">
                                <XIcon
                                    className="absolute top-2 right-2 z-10 cursor-pointer rounded-lg text-red-400 hover:bg-red-500/20"
                                    onClick={() => handleRequestDeleteDraft(d.id)}
                                />
                                <CardHeader>
                                    <div className="flex items-center justify-between gap-2">
                                        <CardTitle className="text-lg">{d.title}</CardTitle>
                                        <Badge variant="secondary" className="-translate-x-6">#{d.id}</Badge>
                                    </div>
                                </CardHeader>
                                <CardContent className="space-y-4">
                                    {d.content?.length ? (
                                        d.content.map((cu: UseCase) => {
                                            const mainFlowItems = splitNumberedItems(cu.main_flow);
                                            const hasNumberedSteps = mainFlowItems.length > 1;

                                            return (
                                                <div key={cu.id} className="relative rounded-md border border-border p-3 space-y-2">
                                                    <div className="flex items-center justify-between gap-2">
                                                        <p className="font-medium text-foreground">Caso #{cu.id}</p>
                                                        <Badge variant="outline">{cu.actor}</Badge>
                                                    </div>
                                                    <div className="space-y-1">
                                                        <p className="text-xs text-muted-foreground">Flujo principal</p>
                                                        {hasNumberedSteps ? (
                                                            <ol className="list-decimal pl-5 text-sm space-y-1">
                                                                {mainFlowItems.map((step, index) => (
                                                                    <li key={`${cu.id}-step-${index}`}>
                                                                        {step.replace(/^\d+\.\s*/, "")}
                                                                    </li>
                                                                ))}
                                                            </ol>
                                                        ) : (
                                                            <p className="text-sm">{cu.main_flow}</p>
                                                        )}
                                                    </div>
                                                    <XIcon
                                                        className="absolute top-2 right-2 size-4 cursor-pointer rounded text-red-400 hover:bg-red-500/20"
                                                        onClick={() => handleRemoveUseCase(d.id, cu.id)}
                                                    />
                                                </div>
                                            );
                                        })
                                    ) : (
                                        <p className="text-sm text-muted-foreground">Sin casos de uso.</p>
                                    )}
                                    <div className="flex items-center justify-center gap-2 pt-2">
                                        <Button onClick={() => handleRequestPublish(d.id)}>Publicar</Button>
                                        <Button variant="outline" onClick={() => handleToggleDetails(d.id)}>Ver detalle</Button>
                                    </div>
                                </CardContent>
                            </Card>
                        )
                    ))}
                </div>
            ) : (
                <p>{searchQuery ? "No se encontraron borradores." : "No hay borradores guardados."}</p>
            )}
            {!showDetails && <DataPagination page={page} totalPages={totalPages} onPageChange={setPage} />}
        </div>
    )
}

export default Draft