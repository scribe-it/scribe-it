import { useCallback, useState } from "react";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ChevronDown, SendHorizontal, Users } from "lucide-react";
import { useStompClient, useSubscription } from "react-stomp-hooks";
import useChat from "@/hooks/use-chat";
import { useQueryClient } from "@tanstack/react-query";

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
  const [showParticipants, setShowParticipants] = useState(false)

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

  const stompClient = useStompClient();

  const handleSendMessage = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/app/chat-send",
        body: JSON.stringify({ chatId, senderId: 1, content: inputValue, type: "TEXT" }),
      });
      setInputValue("");
    } else {
      //Handle error
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
        senderId: 1,
        content: "FW: " + msg.content,
        type: "TEXT",
      }),
    });

    queryClient.setQueryData(["chat", docubotChat.id], (prev: ChatData | undefined) => {
      if (!prev) return undefined;
      return { ...prev, messages: [...prev.messages, { ...msg, content: "FW: " + msg.content }] };
    });
  };

  if(data?.messages) {
    return (
      <Card className="w-full max-w-[50vw] min-w-[50vw] mx-auto h-[600px] flex flex-col">
        {/* Encabezado con estado en tiempo real */}
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3 border-b">
          <div className="flex items-center space-x-3">
            {/* <Avatar> */}
              {/* <AvatarFallback>{companionName[0]}</AvatarFallback> */}
              {/* <AvatarFallback>{"Germán"}</AvatarFallback> */}
            {/* </Avatar> */}
            <button onClick={() => setShowParticipants(!showParticipants)} className="flex items-center gap-1 text-xs text-muted-foreground">
              <Users className="h-3 w-3" />
              Participantes ({data.users.length})
              <ChevronDown className={`h-3 w-3 transition-transform ${showParticipants ? "rotate-180" : ""}`} />
            </button>
            <div>
              <p className="text-sm font-medium leading-none">{"Germán"}</p>
              <p className="text-xs text-muted-foreground mt-1">
                {/* {isOnline ? <Badge variant="success">En línea</Badge> : <Badge variant="secondary">Desconectado</Badge>} */}
                {isOnline ? <Badge>En línea</Badge> : <Badge variant="secondary">Desconectado</Badge>}
              </p>
            </div>
          </div>
        </CardHeader>
  
        {showParticipants && (
        <div className="border-t px-4 py-2 space-y-1">
          {data?.users.map(u => (
            <div key={u.id} className="flex items-center gap-2 text-sm">
              <Avatar className="h-6 w-6"><AvatarFallback>{u.firstName[0]}</AvatarFallback></Avatar>
              <span>{u.firstName} {u.lastName}</span>
              <span className="text-xs text-muted-foreground">({u.email})</span>
            </div>
          ))}
        </div>
      )}

        {/* Cuerpo del chat con ScrollArea */}
        <CardContent className="flex-1 p-4 overflow-hidden">
          <ScrollArea className="h-full pr-4">
            <div className="flex flex-col space-y-4">
              {data?.messages?.map((msg) => {
                // const isMe = msg.senderId === currentUserId;
                const isMe = true; // Solo para pruebas, asume que todos los mensajes son del usuario
                return (
                  <div key={msg.id} className={`flex ${isMe ? "justify-end" : "justify-start"}`}>
                    <ChevronDown className={`size-6 text-muted-foreground ${isMe ? "rotate-270 translate-y-3" : ""}`} onClick={() => handleForwardMessage(msg)}/>
                    <div className={`max-w-[70%] rounded-lg px-3 py-2 text-sm ${
                      isMe ? "bg-primary text-primary-foreground rounded-tr-none" : "bg-muted rounded-tl-none"
                    }`}>
                      <p>{msg.content}</p>
                      <span className="text-[10px] block text-right mt-1 opacity-70">{msg.timestamp}</span>
                    </div>
                  </div>
                );
              })}
            </div>
          </ScrollArea>
        </CardContent>
  
        {/* Input de envío */}
        <CardFooter className="p-3 border-t">
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
}