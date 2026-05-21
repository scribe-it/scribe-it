import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar"
import { AppSidebar } from "@/components/app-sidebar"
import { SecondarySidebar } from "./secondary-sidebar"

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <SidebarProvider
    style={
      {
        "--sidebar-width": "20rem",
        "--sidebar-width-mobile": "20rem",
      } as React.CSSProperties
    }>
      <AppSidebar />
      <main>
        <SidebarTrigger />
        {children}
      </main>
      <SecondarySidebar />
    </SidebarProvider>
  )
}