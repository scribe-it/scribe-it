import { ChatWindow } from "@/components/chat-shadcn"
import Layout from "@/components/layout"

const Home = () => {
  return (
    <Layout>
      <ChatWindow currentUserId={1} companionName={"German"}/>
    </Layout>
  )
}

export default Home