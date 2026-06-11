import { useEffect, useRef, useState } from "react";
import { useStompClient, useSubscription } from "react-stomp-hooks";
import { Button } from "./ui/button";

export const SubscribingComponent = () => {
  const [messages, setMessages] = useState<string[]>(["No message received yet"]);

  const chatId = 1; 

  useSubscription("/topic/chat/" + chatId,  (message) => {
    const newMsg = JSON.parse(message.body);
    setMessages((prev) => [...prev, newMsg]);
  });

  return <div>Last Message: {messages[messages.length - 1]}</div>;
}

export const SendingMessages = () => {
  const stompClient = useStompClient();

  const sendMessage = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/app/chat-send",
        body: JSON.stringify({ chatId: 1, senderId: 1, text: "Echo 123" }),
      });
      
    } else {
      //Handle error
    }
  };

  return <Button onClick={sendMessage}>Send Message</Button>;
}

type Message = {
  id: number;
  text: string;
  sender: "user" | "bot";
};

export default function Chat() {
  const [messages, setMessages] = useState<Message[]>([
    { id: 1, text: "Hola 👋 ¿en qué puedo ayudarte?", sender: "bot" },
  ]);
  const [input, setInput] = useState("");

  const bottomRef = useRef<HTMLDivElement | null>(null);

  const sendMessage = () => {
    if (!input.trim()) return;

    const newMessage: Message = {
      id: Date.now(),
      text: input,
      sender: "user",
    };

    setMessages((prev) => [...prev, newMessage]);
    setInput("");

    setTimeout(() => {
      setMessages((prev) => [
        ...prev,
        {
          id: Date.now() + 1,
          text: "Recibido: " + newMessage.text,
          sender: "bot",
        },
      ]);
    }, 500);
  };

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="flex flex-col h-screen max-w-md mx-auto border border-border rounded-xl overflow-hidden">
      <div className="bg-teal-800 text-white p-3 font-bold">
        Chat
      </div>

      <div className="flex-1 p-3 overflow-y-auto space-y-2 bg-muted/50">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`flex ${
              msg.sender === "user" ? "justify-end" : "justify-start"
            }`}
          >
            <div
              className={`px-3 py-2 rounded-lg max-w-[75%] text-sm ${
                msg.sender === "user"
                  ? "bg-teal-400 text-white"
                  : "bg-card border border-border"
              }`}
            >
              {msg.text}
            </div>
          </div>
        ))}
        <div ref={bottomRef} />
      </div>

      <div className="p-3 border-t border-border flex gap-2 bg-card">
        <input
          className="flex-1 border border-input rounded-lg px-3 py-2 text-sm outline-none bg-transparent"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          placeholder="Escribí un mensaje..."
        />
        <button
          onClick={sendMessage}
          className="bg-teal-400 text-white px-4 rounded-lg text-sm hover:bg-teal-600"
        >
          Enviar
        </button>
      </div>
    </div>
  );
}