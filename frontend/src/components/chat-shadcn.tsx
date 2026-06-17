import { useCallback, useEffect, useState } from "react";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { ChevronDown, ChevronLeft, ChevronRight, SendHorizontal, Users } from "lucide-react";
import { useStompClient, useSubscription } from "react-stomp-hooks";
import useChat from "@/hooks/use-chat";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useAuth } from "@/context/use-auth";

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  department: string;
}

export interface ChatData {
  id:number
  department: string
  description: string
  messages: Message[]
  users: User[]
}

export interface Message {
  id: number;
  content: string;
  user_id: number;
  read: boolean;
  timestamp: string;
}


interface ChatSummary {
  id: number;
  department: string;
}

export function ChatWindow({ chatId }: { chatId: number }) {
  
  const [inputValue, setInputValue] = useState("");
  const [isOnline] = useState(true); 
  const { userData } = useAuth()

  const queryClient = useQueryClient();
  
  const onMessage = useCallback((message: { body: string }) => {
    const newMsg = JSON.parse(message.body);
    queryClient.setQueryData(["chat", chatId], (prev: ChatData | undefined) => {
      if (!prev) return undefined;
      return { ...prev, messages: [...prev.messages, newMsg] };
    });
  }, [chatId, queryClient]);
  
  useSubscription("/topic/chat/" + chatId,  onMessage);
  
  const { data } = useChat(chatId);
  
  useEffect(() => {
    fetch("/api/v1/chat/" + chatId + "/read", {
      method: "PATCH",
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    }).catch(console.error);
    queryClient.setQueryData(["unreadCounts"], (prev: Record<number, number> | undefined) => {
      if (!prev) return undefined;
      return { ...prev, [chatId]: 0 };
    });
  }, [chatId, queryClient]);

  const stompClient = useStompClient();

  const handleSendMessage = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/app/chat-send",
        body: JSON.stringify({ chatId, senderId: userData?.id, content: inputValue, type: "TEXT" }),
      });
      setInputValue("");
    } else {
      toast.error("No se pudo enviar el mensaje. Intenta nuevamente.");
    }
  };

  const handleForwardMessage = async (msg: Message) => {
    if (!stompClient) return;
    const response = await fetch("/api/v1/chat", {
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
    });
    if (!response.ok) return;

    const chats: ChatSummary[] = await response.json();
    const docubotChat = chats.find((chat) => chat.department.toLowerCase() === "docubot");
    if (!docubotChat) return;

    stompClient.publish({
      destination: "/app/chat-send",
      body: JSON.stringify({
        chatId: docubotChat.id,
        senderId: userData?.id,
        content: "FW: " + msg.content,
        type: "TEXT",
      }),
    });

    queryClient.setQueryData(["chat", docubotChat.id], (prev: ChatData | undefined) => {
      if (!prev) return undefined;
      return { ...prev, messages: [...prev.messages, { ...msg, content: "FW: " + msg.content }] };
    });

    toast.success("Mensaje reenviado a Docubot");
  };

  if(!data?.messages) {
    return (
      <Card className="flex-1 h-[100vh] flex items-center justify-center rounded-none pt-0">
        Selecciona un chat para comenzar a conversar
      </Card>
    )
  }

  return (
    <Card className="flex-1 h-[100vh] flex flex-col rounded-none pt-0">
      {/* Encabezado con estado en tiempo real */}
      <CardHeader className="flex flex-row items-center justify-between bg-[#1e1e1c] p-4 border-b border-white/[0.07]">
        <div className="flex items-center space-x-3">
          {/* <Avatar> */}
            {/* <AvatarFallback>{companionName[0]}</AvatarFallback> */}
            {/* <AvatarFallback>{"Germán"}</AvatarFallback> */}
          {/* </Avatar> */}
          <div>
            <p className="text-sm font-medium leading-none">{userData?.username}</p>
            <p className="text-xs text-muted-foreground mt-1">
              {/* {isOnline ? <Badge variant="success">En línea</Badge> : <Badge variant="secondary">Desconectado</Badge>} */}
              {isOnline ? <Badge>En línea</Badge> : <Badge variant="secondary">Desconectado</Badge>}
            </p>
          </div>
        </div>
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <button className="flex items-center text-muted-foreground gap-2">
                <Users className="size-4" />
                <p className="text-lg">Participantes ({data.users.length})</p>
                <ChevronDown className="size-4" />
              </button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="start" className="w-80">
              <DropdownMenuLabel>Participantes ({data.users.length})</DropdownMenuLabel>
              <DropdownMenuSeparator />
              <div className="max-h-64 space-y-1 overflow-y-auto p-1">
                {data?.users.map((u) => (
                  <div key={u.id} className="flex items-center gap-2 rounded-md px-2 py-1 text-sm">
                    <Avatar className="h-6 w-6"><AvatarFallback>{u.firstName[0]}</AvatarFallback></Avatar>
                    <span>{u.firstName} {u.lastName}</span>
                    <span className="text-xs text-muted-foreground">({u.email})</span>
                  </div>
                ))}
              </div>
            </DropdownMenuContent>
          </DropdownMenu>
      </CardHeader>

      <CardContent className="flex-1 p-4 overflow-hidden">
        <ScrollArea className="h-full pr-4">
          <div className="flex flex-col space-y-4">
            {data?.messages?.map((msg) => {
              // const isMe = msg.senderId === currentUserId;
              const isMe = msg.user_id === userData?.id;
              return (
                <div key={msg.id} className={`flex ${isMe ? "justify-end" : "justify-start"}`}>
                  {isMe && <ChevronRight className={`size-8 text-muted-foreground hover:bg-white transition-transform duration-100 active:scale-75 rounded-full m-4`} onClick={() => handleForwardMessage(msg)}/>}
                    <div className={`max-w-[70%] rounded-lg px-3 py-2 text-sm ${
                    isMe ? "bg-primary text-primary-foreground rounded-tr-none" : "bg-muted rounded-tl-none"
                  }`}>
                    <p>{msg.content}</p>
                    <span className="text-[10px] block text-right mt-1 opacity-70">{msg.timestamp}</span>
                  </div>
                  {
                    !isMe && <ChevronLeft className={`size-8 text-muted-foreground hover:bg-white transition-transform duration-100 active:scale-75 rounded-full m-4`} onClick={() => handleForwardMessage(msg)}/>
                  }
                </div>
              );
            })}
          </div>
        </ScrollArea>
      </CardContent>

      <CardFooter className="p-3 bg-[#1e1e1c] border-t border-white/[0.07]">
        <div className="flex w-full items-center space-x-2">
          <Input
            placeholder="Escribe un mensaje..."
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSendMessage()}
          />
          <Button size="icon" onClick={handleSendMessage}>
            <SendHorizontal className="h-4 w-4" />
          </Button>
        </div>
      </CardFooter>
    </Card>
  );
}