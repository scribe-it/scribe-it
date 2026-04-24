import { useEffect, useRef, useState } from "react";

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
    <div className="flex flex-col h-screen max-w-md mx-auto border rounded-xl overflow-hidden">
      <div className="bg-black text-white p-3 font-bold">
        Chat
      </div>

      <div className="flex-1 p-3 overflow-y-auto space-y-2 bg-gray-100">
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
                  ? "bg-blue-500 text-white"
                  : "bg-white border"
              }`}
            >
              {msg.text}
            </div>
          </div>
        ))}
        <div ref={bottomRef} />
      </div>

      <div className="p-3 border-t flex gap-2 bg-white">
        <input
          className="flex-1 border rounded-lg px-3 py-2 text-sm outline-none"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          placeholder="Escribí un mensaje..."
        />
        <button
          onClick={sendMessage}
          className="bg-blue-500 text-white px-4 rounded-lg text-sm"
        >
          Enviar
        </button>
      </div>
    </div>
  );
}