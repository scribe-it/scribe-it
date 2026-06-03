import { Message } from '@/components/chat-shadcn';
import { useQuery } from '@tanstack/react-query';

interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  department: string;
}

interface ChatData {
  id: number
  department: string
  description: string
  messages: Message[]
  users: User[]
}

const useChat = (chatId: number) => {

    const {
        data
    } = useQuery<ChatData[]>({
        queryKey: ["chat", chatId],
        queryFn: async () => {
        const response = await fetch(`/api/v1/chat/${chatId}`, {
            headers: { 'Authorization': `Bearer ${localStorage.getItem("token")}` }
        });
            if (!response.ok) { 
                throw new Error("Network response was not ok");
            }
            return response.json();
        },
        staleTime: 30000,
    })

    return { data };

    }
    
    export default useChat