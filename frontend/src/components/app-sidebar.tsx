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
import { ChevronDown, Code, FileText, MessageSquare, Moon, Plus, Sun, UserPlus } from "lucide-react"
import { useTheme } from "@/hooks/use-theme"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "./ui/dropdown-menu"
import { Link, useLocation } from "react-router"
  
const items = [
  // {name: "chats", url: "chats", icon: MessageSquare},
  {name: "chats", url: "", icon: MessageSquare},
  {name:"editor", url: "editor", icon: Code},
  {name:"publicaciones", url: "publicaciones", icon: FileText},
] as const

  export function AppSidebar() {
    const { theme, toggle } = useTheme()
    const { pathname } = useLocation()

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
            {items.map((project) => (
              <SidebarMenuItem key={project.name}>
                <SidebarMenuButton asChild isActive={pathname === `/${project.url}`}>
                  <Link to={project.url} >
                    <project.icon />
                    <span>{project.name.charAt(0).toUpperCase() + project.name.slice(1)}</span>
                  </Link>
                </SidebarMenuButton>
              </SidebarMenuItem>
            ))}
              <SidebarMenuItem>
                <SidebarMenuButton>
                  <UserPlus />
                  <span>Registrar usuario</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
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
          </SidebarMenu>
        </SidebarFooter>
      </Sidebar>
    )
  }