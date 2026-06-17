import React, { useState } from 'react'
import { Card } from './ui/card'
import { Input } from './ui/input'
import { cn } from '@/lib/utils'
import { Button } from './ui/button'
import { Check, Plus, X } from 'lucide-react'
import { useMutation, useQueryClient } from '@tanstack/react-query'

interface ChatFormProps {
    showForm: boolean
    setShowForm: React.Dispatch<React.SetStateAction<boolean>>
}

const ChatForm = ({ showForm, setShowForm }: ChatFormProps) => {
    
    const [chatName, setChatName] = useState("")
    const queryClient = useQueryClient();

    const {
        mutateAsync: createChat,
        isPending: loading
    } = useMutation({
        mutationFn: async (name: string) => {
            const response = await fetch("/api/v1/chat", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify({ department: name.trim(), description: "Nuevo chat" }),
            })
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["chats"] });
            setChatName("")
            setShowForm(false)
        }
    })

    const handleCreateChat = async () => { if (chatName.trim()) await createChat(chatName) }

    
    const handleCancel = () => {
        setShowForm(false)
        setChatName("")
    }
    
  return (
    <Card className={cn("p-2 flex items-center gap-2 ring-0 justify-center size-10", showForm ? "bg-transparent w-40 h-32" : "bg-teal-400 text-white")} onClick={() => !showForm && setShowForm(true)}>
        {showForm ? (
            <>
                <Input
                    value={chatName}
                    onChange={(e) => setChatName(e.target.value)}
                    placeholder="Nombre del departamento"
                    onKeyDown={(e) => e.key === "Enter" && handleCreateChat()}
                    className="h-8 text-sm"
                    autoFocus
                    disabled={loading}
                />
                <Button size="icon" variant="ghost" onClick={handleCreateChat} disabled={loading}>
                    <Check className="h-4 w-4 text-teal-600" />
                </Button>
                <Button size="icon" variant="ghost" onClick={handleCancel} disabled={loading}>
                    <X className="h-4 w-4 text-red-600" />
                </Button>
            </>
        ) : (
            <Button variant="ghost" className=" gap-2" onClick={() => setShowForm(true)}>
                <Plus className="h-4 w-4" />
            </Button>
        )}
    </Card>
  )
}

export default ChatForm