import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar"
import { AppSidebar } from "@/components/app-sidebar"
import { SecondarySidebar } from "./secondary-sidebar"

export default function Layout({ children, setChatId, setView }: { children: React.ReactNode, setChatId: React.Dispatch<React.SetStateAction<number>>, setView: React.Dispatch<React.SetStateAction<"chat" | "register">> }) {

  return (
    <SidebarProvider
    style={
      {
        "--sidebar-width": "20rem",
        "--sidebar-width-mobile": "20rem",
      } as React.CSSProperties
    }>
      <AppSidebar setView={setView} />
      <main>
        <SidebarTrigger />
        {children}
      </main>
      <SecondarySidebar setChatId={setChatId} />
    </SidebarProvider>
  )
}