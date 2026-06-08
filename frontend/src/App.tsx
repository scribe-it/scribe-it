import './App.css'
import { StompSessionProvider } from "react-stomp-hooks";
import { AuthProvider } from "./context/auth-provider";
import { useAuth } from "./context/use-auth";
import LoginPage from "./pages/log-in";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { RouterProvider } from "react-router-dom";
import { router } from "./routes/router";

const queryClient = new QueryClient();
  
function App() {
  const { token } = useAuth();
  if (!token) return <LoginPage />;
  return (
    <AuthProvider>
      <QueryClientProvider client={queryClient}>
        <StompSessionProvider
          url="ws://localhost:8080/ws-stomp"
          brokerURL={"ws://localhost:8080/ws-stomp"}
          connectHeaders={{ Authorization: `Bearer ${token}` }}
          debug={(str) => console.log('[STOMP]', str)}
            onStompError={(frame) => console.error('[STOMP ERROR]', frame)}
        >
          <RouterProvider router={router} />
        </StompSessionProvider>
      </QueryClientProvider>
    </AuthProvider>
  );
}

export default App
