import { Outlet } from "react-router-dom"
import { useTheme } from "@/hooks/use-theme"
import { Moon, Sun } from "lucide-react"

const AuthLayout = () => {
  const { theme, toggle } = useTheme()

  return (
    <div className="min-h-svh bg-background text-foreground">
      <button
        onClick={toggle}
        className="fixed top-4 right-4 z-50 text-muted-foreground hover:text-foreground"
      >
        {theme === "dark" ? <Sun className="size-5" /> : <Moon className="size-5" />}
      </button>
      <Outlet />
    </div>
  )
}

export default AuthLayout