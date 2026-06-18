import { useState, type ReactNode } from "react";
import { AuthContext } from "./use-auth";

const API_URL = "http://localhost:8080";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem("token"));
  const [userData, setUserData] = useState<{
    id: number;
    role: string;
    username: string;
    department: string;
    firstName: string;
    lastName: string;
  } | null>(() => {
  const stored = localStorage.getItem("userData");
  return stored ? JSON.parse(stored) : null;
});
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
    const userData = { id: data.userId, role: data.role, username: data.username, department: data.department, firstName: data.firstName, lastName: data.lastName };
    setUserData(userData);
    localStorage.setItem("userData", JSON.stringify(userData));
  };
  const logout = () => {
    setToken(null);
    setUserData(null);
    localStorage.removeItem("token");
    localStorage.removeItem("userData");
  };
  return (
    <AuthContext.Provider value={{ token, login, logout, userData }}>
      {children}
    </AuthContext.Provider>
  );
}
