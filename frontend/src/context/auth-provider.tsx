import { useState, type ReactNode } from "react";
import { AuthContext } from "./use-auth";

const API_URL = "http://localhost:8080";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(null);
  const login = async (email: string, password: string) => {
    const res = await fetch(`${API_URL}/api/v1/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });
    if (!res.ok) throw new Error("Credenciales inválidas");
    const data = await res.json();
    localStorage.setItem("token", data.token);
    setToken(data.token);
  };
  const logout = () => {
    setToken(null);
    localStorage.removeItem("token");
  }
  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
