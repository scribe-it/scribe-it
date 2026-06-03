import { ChatWindow } from "@/components/chat-shadcn"
import Layout from "@/components/layout"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { useState } from "react";
import RegisterUser from "./register-user";

const queryClient = new QueryClient();

const Home = () => {

  const [chatId, setChatId] = useState(0);
  const [view, setView] = useState<"chat" | "register">("chat");
  
  return (
    <QueryClientProvider client={queryClient}>
      <Layout setChatId={setChatId} setView={setView}>
        {view === "chat" ? <ChatWindow chatId={chatId} /> : <RegisterUser />}
      </Layout>
    </QueryClientProvider>
  )
}

export default Home