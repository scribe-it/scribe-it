import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarGroup,
    SidebarGroupAction,
    SidebarGroupContent,
    SidebarGroupLabel,
    SidebarHeader,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    SidebarTrigger,
  } from "@/components/ui/sidebar"
import { Code, FilePenLine, FileText, LogOut, MessageSquare, Moon, Plus, Sun, UserPlus } from "lucide-react"
import { useTheme } from "@/hooks/use-theme"
import { useAuth } from "@/context/use-auth"
import { Link, useLocation } from "react-router"
  
const items = [
  // {name: "chats", url: "chats", icon: MessageSquare},
  {name: "chats", url: "", icon: MessageSquare},
  {name:"editor", url: "editor", icon: Code},
  {name:"borradores", url: "drafts", icon: FilePenLine},
  {name:"publicaciones", url: "publicaciones", icon: FileText},
  {name:"registrar usuario", url: "register-user", icon: UserPlus},
] as const

  export function AppSidebar() {
    const { theme, toggle } = useTheme()
    const { pathname } = useLocation()
    const { userData, logout } = useAuth()

    const isAnalyst = !userData?.department

    return (
      <Sidebar collapsible="icon" className="border-r border-white/[0.07] bg-[#222220]">
          <SidebarHeader className="relative">
            <SidebarMenu>
              <SidebarMenuItem>
                <SidebarTrigger className="text-white" />
              </SidebarMenuItem>
            </SidebarMenu>
          </SidebarHeader>
        <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Application</SidebarGroupLabel>
          <SidebarGroupAction>
            <Plus /> <span className="sr-only">Add Project</span>
          </SidebarGroupAction>
          <SidebarGroupContent></SidebarGroupContent>
          <SidebarMenu>
            {isAnalyst && items.map((item) => (
              <SidebarMenuItem key={item.name}>
                <SidebarMenuButton asChild isActive={pathname === `/${item.url}`}>
                  <Link to={item.url} >
                    <item.icon />
                    <span>{item.name.charAt(0).toUpperCase() + item.name.slice(1)}</span>
                  </Link>
                </SidebarMenuButton>
              </SidebarMenuItem>
            ))}
            {!isAnalyst && items.filter((item) => ["", "publicaciones"].includes(item.url)).map((item) => (
              <SidebarMenuItem key={item.name}>
                <SidebarMenuButton asChild isActive={pathname === `/${item.url}`}>
                  <Link to={item.url} >
                    <item.icon />
                    {item.name === "publicaciones" && <span>{item.name.charAt(0).toUpperCase() + item.name.slice(1)}</span>}
                    {item.name === "chats" && <span>Chat</span>}
                  </Link>
                </SidebarMenuButton>
              </SidebarMenuItem>
            ))}
          </SidebarMenu>
        </SidebarGroup>
          <SidebarGroup />
        </SidebarContent>
        <SidebarFooter>
          <SidebarMenu>
            <SidebarMenuItem>
              <SidebarMenuButton onClick={toggle}>
                {theme === "dark" ? <Sun className="size-4" /> : <Moon className="size-4" />}
                <span>{theme === "dark" ? "Modo claro" : "Modo oscuro"}</span>
              </SidebarMenuButton>
            </SidebarMenuItem>
            <SidebarMenuItem>
              <SidebarMenuButton onClick={logout}>
                <LogOut className="size-4" />
                <span>Cerrar sesión</span>
              </SidebarMenuButton>
            </SidebarMenuItem>
          </SidebarMenu>
        </SidebarFooter>
      </Sidebar>
    )
  }