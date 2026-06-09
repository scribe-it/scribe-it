import { SidebarProvider } from "@/components/ui/sidebar"
import { AppSidebar } from "@/components"
import { Navigate, Outlet } from "react-router"
import { useAuth } from "@/context/use-auth";
import { Routes } from '../routes/paths'
import { StompSessionProvider } from "react-stomp-hooks";

export default function Layout() {
  const { token } = useAuth();
  if (!token) return <Navigate to={Routes.logIn} replace />;

  return (
    <StompSessionProvider
          url="ws://localhost:8080/ws-stomp"
          brokerURL={"ws://localhost:8080/ws-stomp"}
          connectHeaders={{ Authorization: `Bearer ${token}` }}
          debug={(str) => console.log('[STOMP]', str)}
            onStompError={(frame) => console.error('[STOMP ERROR]', frame)}
        >
        <SidebarProvider
        style={
          {
            "--sidebar-width": "20rem",
            "--sidebar-width-mobile": "20rem",
          } as React.CSSProperties
        }>
          <AppSidebar />
          <Outlet />
        </SidebarProvider>
      </StompSessionProvider>
  )
}