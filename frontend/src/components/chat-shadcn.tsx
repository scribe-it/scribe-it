import { useState } from "react";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { SendHorizontal } from "lucide-react";
import { useStompClient, useSubscription } from "react-stomp-hooks";

interface Message {
  id: number;
  content: string;
  user_id: number;
  read: boolean;
  timestamp: string;
}

export function ChatWindow() {
  const [inputValue, setInputValue] = useState("");
  const [messages, setMessages] = useState<Message[]>([]);
  const [isOnline] = useState(true); 

  const chatId = 1; 

  useSubscription("/topic/chat/" + chatId,  (message) => {
     console.log("[TOPIC]2", message.body);
    const newMsg = JSON.parse(message.body);
    setMessages((prev) => [...prev, newMsg]);
  });

  const stompClient = useStompClient();

  const handleSendMessage = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/app/chat-send",
        body: JSON.stringify({ chatId: 1, senderId: 1, content: inputValue, type: "TEXT" }),
      });
      setInputValue("");
    } else {
      //Handle error
    }
  };
  return (
    <Card className="w-full max-w-[50vw] min-w-[50vw] mx-auto h-[600px] flex flex-col">
      {/* Encabezado con estado en tiempo real */}
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3 border-b">
        <div className="flex items-center space-x-3">
          <Avatar>
            {/* <AvatarFallback>{companionName[0]}</AvatarFallback> */}
            <AvatarFallback>{"Germán"}</AvatarFallback>
          </Avatar>
          <div>
            <p className="text-sm font-medium leading-none">{"Germán"}</p>
            <p className="text-xs text-muted-foreground mt-1">
              {/* {isOnline ? <Badge variant="success">En línea</Badge> : <Badge variant="secondary">Desconectado</Badge>} */}
              {isOnline ? <Badge>En línea</Badge> : <Badge variant="secondary">Desconectado</Badge>}
            </p>
          </div>
        </div>
      </CardHeader>

      {/* Cuerpo del chat con ScrollArea */}
      <CardContent className="flex-1 p-4 overflow-hidden">
        <ScrollArea className="h-full pr-4">
          <div className="flex flex-col space-y-4">
            {messages.map((msg) => {
              // const isMe = msg.senderId === currentUserId;
              const isMe = true; // Solo para pruebas, asume que todos los mensajes son del usuario
              return (
                <div key={msg.id} className={`flex ${isMe ? "justify-end" : "justify-start"}`}>
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