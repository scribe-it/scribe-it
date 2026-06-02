import './App.css'
import { StompSessionProvider } from "react-stomp-hooks";
import { AuthProvider } from "./context/auth-provider";
import { useAuth } from "./context/use-auth";
import Home from "./pages/home";
import LoginPage from "./pages/login";

function AppContent() {
  const { token } = useAuth();
  if (!token) return <LoginPage />;
  return (
    <StompSessionProvider
      url="ws://localhost:8080/ws-stomp"
      brokerURL={"ws://localhost:8080/ws-stomp"}
      connectHeaders={{ Authorization: `Bearer ${token}` }}
      debug={(str) => console.log('[STOMP]', str)}
        onStompError={(frame) => console.error('[STOMP ERROR]', frame)}
    >
      <Home />
    </StompSessionProvider>
  );
}
function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App
