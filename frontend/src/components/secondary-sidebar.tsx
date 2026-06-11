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
        <aside className="w-56 m-10">
            <div className="flex flex-col gap-4">
                <div className="flex items-center justify-between">
                <header className="mb-2 font-medium text-foreground">Departamentos</header>
                <Card className={cn("p-2 flex items-center gap-2 ring-0 justify-center size-10", showInput ? "bg-transparent" : "bg-teal-400 text-white")} onClick={() => !showInput && setShowInput(true)}>
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
                                <Check className="h-4 w-4 text-teal-600" />
                            </Button>
                            <Button size="icon" variant="ghost" onClick={handleCancel} disabled={loading}>
                                <X className="h-4 w-4 text-red-600" />
                            </Button>
                        </>
                    ) : (
                        <Button variant="ghost" className=" gap-2" onClick={() => setShowInput(true)}>
                            <Plus className="h-4 w-4" />
                        </Button>
                    )}
                </Card>
                </div>
                {chats.filter(c => c.department !== "Docubot").map((chat) => (
                    <Card key={chat.id} className={cn("w-56 p-2 flex items-center justify-between cursor-pointer ring-0 bg-transparent", chat.id === chatId ? "bg-teal-400/15 border-teal-700 text-teal-300 ring-1 border-1 rounded-lg" : "")} onClick={() => setChatId(chat.id)}>
                        <div className="flex justify-between w-full">
                            <span>{chat.department.split(" ").slice(0, chat.department.split(" ").length - 2).join(" ")}</span>
                            <span>{chat.department.split(" ").slice(chat.department.split(" ").length - 1).join(" ")}</span>
                            {/* <span className={cn("text-xs font-bold opacity-60", chat.id === chatId ? "text-white" : "text-muted-foreground")}>{abbr(chat.department)}</span> */}
                        </div>  
                    </Card>
                ))}
                {chats.find(c => c.department === "Docubot") && (() => {
                    const docubot = chats.find(c => c.department === "Docubot")!
                    return (
                        <Card key={0} className={cn("p-2 flex items-center justify-between cursor-pointer ring-0", docubot.id === chatId ? "bg-teal-400 text-white" : "")} onClick={() => setChatId(docubot.id)}>
                            <span>{docubot.department.split(" ")[0]}</span>
                        </Card>
                    )
                })()}
            </div>
        </aside>
    )
}
