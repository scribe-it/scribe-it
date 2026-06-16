import { createContext, useContext } from "react";

export const AuthContext = createContext<AuthContextType | null>(null);

interface AuthContextType {
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  userData: { id: number, username: string; department: string } | null;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}