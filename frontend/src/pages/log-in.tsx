import { useAuth } from "@/context/use-auth";
import { Routes } from "@/routes/paths";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useAuth()
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    try {
      await login(email, password);
      navigate(Routes.home)
    } catch {
      setError("Credenciales inválidas");
    }
  };
  return (
    <div className="flex items-center justify-center h-screen bg-muted/30">
      <form onSubmit={handleSubmit} className="flex flex-col w-[600px] bg-card rounded-xl shadow-panel overflow-hidden">
        <img src={"/docubot_logo_dark.png"} alt="Logo" className="w-full" />
        <div className="flex flex-col gap-4 p-8 pt-6">
          {error && <p className="text-destructive text-sm">{error}</p>}
          <Input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <Input
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <Button type="submit" className="w-full">
            Ingresar
          </Button>
        </div>
      </form>
    </div>
  );
}