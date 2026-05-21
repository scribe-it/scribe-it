import { useState, useEffect, useRef } from "react";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { SendHorizontal } from "lucide-react";

// Supongamos que tu instancia de socket viene de un hook o contexto externo
// import { useSocket } from "@/context/SocketContext";

interface Message {
  id: string;
  text: string;
  senderId: string;
  senderName: string;
  timestamp: string;
}

export function ChatWindow({ currentUserId, companionName }) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputValue, setInputValue] = useState("");
  const [isOnline, setIsOnline] = useState(false);
  // const { socket } = useSocket();

  // Escuchar mensajes entrantes del socket
  useEffect(() => {
    /* 
    socket.on("receive_message", (newMessage: Message) => {
      setMessages((prev) => [...prev, newMessage]);
    });

    socket.on("user_status_change", (status: boolean) => {
      setIsOnline(status);
    });

    return () => {
      socket.off("receive_message");
      socket.off("user_status_change");
    };
    */
  }, []);

  const handleSendMessage = () => {
    if (!inputValue.trim()) return;

    const messageData = {
      id: crypto.randomUUID(),
      text: inputValue,
      senderId: currentUserId,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    };

    // 1. Emitir por el socket al servidor
    // socket.emit("send_message", messageData);

    // 2. Actualizar la UI localmente
    setMessages((prev) => [...prev, messageData]);
    setInputValue("");
  };

  return (
    <Card className="w-full max-w-[50vw] min-w-[50vw] mx-auto h-[600px] flex flex-col">
      {/* Encabezado con estado en tiempo real */}
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3 border-b">
        <div className="flex items-center space-x-3">
          <Avatar>
            <AvatarFallback>{companionName[0]}</AvatarFallback>
          </Avatar>
          <div>
            <p className="text-sm font-medium leading-none">{companionName}</p>
            <p className="text-xs text-muted-foreground mt-1">
              {isOnline ? <Badge variant="success">En línea</Badge> : <Badge variant="secondary">Desconectado</Badge>}
            </p>
          </div>
        </div>
      </CardHeader>

      {/* Cuerpo del chat con ScrollArea */}
      <CardContent className="flex-1 p-4 overflow-hidden">
        <ScrollArea className="h-full pr-4">
          <div className="flex flex-col space-y-4">
            {messages.map((msg) => {
              const isMe = msg.senderId === currentUserId;
              return (
                <div key={msg.id} className={`flex ${isMe ? "justify-end" : "justify-start"}`}>
                  <div className={`max-w-[70%] rounded-lg px-3 py-2 text-sm ${
                    isMe ? "bg-primary text-primary-foreground rounded-tr-none" : "bg-muted rounded-tl-none"
                  }`}>
                    <p>{msg.text}</p>
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