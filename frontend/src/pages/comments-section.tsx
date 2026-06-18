import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { MessageSquare, Send } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";

type Comment = {
  id: number;
  text: string;
  authorName: string;
  createdAt: string;
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

export const CommentsSection = ({ documentId }: { documentId: number }) => {
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