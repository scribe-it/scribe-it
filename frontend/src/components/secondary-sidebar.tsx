import { useEffect, useState } from "react"
import { cn } from "@/lib/utils"
import { Card } from "./ui/card"
import { Button } from "./ui/button"
import { Input } from "./ui/input"
import { Plus, Check, X } from "lucide-react"

export type Chat = {
    id: number
    department: string
    description: string
}

export const SecondarySidebar = ({ chatId, setChatId }: { chatId: number, setChatId: React.Dispatch<React.SetStateAction<number>> }) => {
    const [chats, setChats] = useState<Chat[]>([])
    const [showInput, setShowInput] = useState(false)
    const [chatName, setChatName] = useState("")
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        fetch("/api/v1/chat", {
            headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        })
            .then((res) => res.json())
            .then((data) => {
                setChats(data)
                setChatId(data[0]?.id || 0)
            })
            .catch(console.error)
    }, [])

    const handleCreate = async () => {
        if (!chatName.trim()) return
        setLoading(true)
        try {
            await fetch("/api/v1/chat", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify({ department: chatName.trim(), description: "Nuevo chat" }),
            })
            setChatName("")
            setShowInput(false)
            setChats((prev) => [...prev, { id: Date.now(), department: chatName.trim(), description: "Nuevo chat" }])
        } catch (err) {
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    const handleCancel = () => {
        setShowInput(false)
        setChatName("")
    }
    return (
        <aside className="px-20 py-10">
            <header className="mb-2">Departamentos</header>
            <div className="flex flex-col gap-4">
                <Card className="p-2 flex items-center gap-2">
                    {showInput ? (
                        <>
                            <Input
                                value={chatName}
                                onChange={(e) => setChatName(e.target.value)}
                                placeholder="Nombre del departamento"
                                onKeyDown={(e) => e.key === "Enter" && handleCreate()}
                                className="h-8 text-sm"
                                autoFocus
                                disabled={loading}
                            />
                            <Button size="icon" variant="ghost" onClick={handleCreate} disabled={loading}>
                                <Check className="h-4 w-4 text-green-600" />
                            </Button>
                            <Button size="icon" variant="ghost" onClick={handleCancel} disabled={loading}>
                                <X className="h-4 w-4 text-red-600" />
                            </Button>
                        </>
                    ) : (
                        <Button variant="ghost" className="w-full justify-start gap-2" onClick={() => setShowInput(true)}>
                            <Plus className="h-4 w-4" />
                            Nuevo departamento
                        </Button>
                    )}
                </Card>
                {chats.filter(c => c.department !== "Docubot").map((chat) => (
                    <Card key={chat.id} className={cn("p-2", chat.id === chatId ? "bg-blue-400 text-white" : "")} onClick={() => setChatId(chat.id)}>
                        {chat.department}
                    </Card>
                ))}
                {chats.find(c => c.department === "Docubot") && (
                        <Card key={0} className="p-2 bg-blue-400 text-white" onClick={() => setChatId(chats.find(c => c.department === "Docubot")!.id)}>
                            Docubot
                        </Card>
                )}
            </div>
        </aside>
    )
}
