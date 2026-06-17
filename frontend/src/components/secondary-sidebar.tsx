import { useState } from "react"
import { cn } from "@/lib/utils"
import { Card } from "./ui/card"
import { Badge } from "./ui/badge"
import { useQuery, useQueryClient } from "@tanstack/react-query"
import { useSubscription } from "react-stomp-hooks"
import ChatForm from "./chat-form"

export type Chat = {
    id: number
    department: string
    description: string
    unreadCount?: number
}

export const SecondarySidebar = ({ chatId, setChatId }: { chatId: number, setChatId: React.Dispatch<React.SetStateAction<number>> }) => {
    
    const [showForm, setShowForm] = useState(false)

    const queryClient = useQueryClient();

    const {
        data: chats
    } = useQuery<Chat[]>({
        queryKey: ["chats"],
        queryFn: async () => {
            const response = await fetch("/api/v1/chat", {
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            if (!response.ok) { 
                throw new Error("Network response was not ok");
            }
            const chats = await response.json()
            const counts: Record<number, number> = {};
            chats.forEach((chat: Chat) => {
                counts[chat.id] = chat.unreadCount || 0;
            });
            queryClient.setQueryData(["unreadCounts"], counts)
            return chats;
        }
    })

    useSubscription("/user/queue/unread", (message) => {
        const data = JSON.parse(message.body);
        queryClient.setQueryData(["unreadCounts"], (prev: Record<number, number> | undefined) => {
            if (!prev) return { [data.chatId]: data.count };
            return { ...prev, [data.chatId]: data.count };
        });
    })

    const { data: unreadCounts = {} } = useQuery({
        queryKey: ["unreadCounts"],
        queryFn: () => ({} as Record<number, number>),
        staleTime: Infinity,
        retry: false,
    })

    return (
        <aside className="w-72 p-10 bg-[#1e1e1c] border-l border-white/[0.07]">
            <div className="flex flex-col gap-4">
                <div className="flex items-center justify-between">
                {!showForm && <header className="mb-2 font-medium text-foreground">Departamentos</header>}
                <ChatForm showForm={showForm} setShowForm={setShowForm} />
                </div>
                {chats?.filter(c => c.department !== "Docubot").map((chat) => (
                    <Card key={chat.id} className={cn("w-56 p-2 relative overflow-visible flex items-center justify-between cursor-pointer ring-0 bg-transparent", chat.id === chatId ? "bg-teal-400/15 border-teal-700 text-teal-300 ring-1 border-1 rounded-lg" : "")} onClick={() => setChatId(chat.id)}>
                        {unreadCounts && unreadCounts[chat.id] > 0 && <Badge className="absolute -top-2 -right-3">{unreadCounts[chat.id]}</Badge>}
                        <div className="flex justify-between w-full">
                            <span>{chat.department.split(" ").slice(0, chat.department.split(" ").length - 2).join(" ")}</span>
                            <span>{chat.department.split(" ").slice(chat.department.split(" ").length - 1).join(" ")}</span>
                            {/* <span className={cn("text-xs font-bold opacity-60", chat.id === chatId ? "text-white" : "text-muted-foreground")}>{abbr(chat.department)}</span> */}
                        </div>  
                    </Card>
                ))}
                {chats?.find(c => c.department === "Docubot") && (() => {
                    const docubot = chats.find(c => c.department === "Docubot")!
                    return (
                        <Card key={0} className={cn("p-2 relative flex items-center justify-between cursor-pointer ring-0", docubot.id === chatId ? "bg-teal-400 text-white" : "")} onClick={() => setChatId(docubot.id)}>
                            {unreadCounts && unreadCounts[docubot.id] > 0 && <Badge className="absolute -top-1 -right-1">{unreadCounts[docubot.id]}</Badge>}
                            <span>{docubot.department.split(" ")[0]}</span>
                        </Card>
                    )
                })()}
            </div>
        </aside>
    )
}
