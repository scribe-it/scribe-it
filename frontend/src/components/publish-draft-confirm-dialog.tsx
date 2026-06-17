import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

type PublishDraftConfirmDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onCancel: () => void;
  onConfirm: () => void;
  isPending?: boolean;
};

export const PublishDraftConfirmDialog = ({
  open,
  onOpenChange,
  onCancel,
  onConfirm,
  isPending = false,
}: PublishDraftConfirmDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Publicar borrador</DialogTitle>
          <DialogDescription>
            Esta accion publicara el borrador actual. Verifica el contenido antes de continuar.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button
            type="button"
            variant="outline"
            onClick={onCancel}
            disabled={isPending}
          >
            Cancelar
          </Button>
          <Button type="button" onClick={onConfirm} disabled={isPending}>
            {isPending ? "Publicando..." : "Publicar"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
