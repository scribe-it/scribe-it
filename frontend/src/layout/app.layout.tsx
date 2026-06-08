import { SidebarProvider } from "@/components/ui/sidebar"
import { AppSidebar } from "@/components"
import { Outlet } from "react-router"

export default function Layout() {

  return (
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
  )
}