import { ChatWindow } from "@/components/chat-shadcn"
import { useState } from "react";
import { SidebarTrigger } from "@/components/ui/sidebar";
import { SecondarySidebar } from "@/components/secondary-sidebar";


const Home = () => {

  const [chatId, setChatId] = useState(0);
  
  return (
        <main>
          <SidebarTrigger />
          <ChatWindow chatId={chatId} />
          <SecondarySidebar chatId={chatId} setChatId={setChatId} />
        </main>
  )
}

export default Home