import { ChatWindow } from "@/components/chat-shadcn"
import { useState } from "react";
import { SecondarySidebar } from "@/components";


const Home = () => {

  const [chatId, setChatId] = useState(0);
  
  return (
        <main className="flex w-full bg-background">
          <ChatWindow chatId={chatId} />
          <SecondarySidebar chatId={chatId} setChatId={setChatId} />
        </main>
  )
}

export default Home