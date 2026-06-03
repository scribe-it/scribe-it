let token: string | null = null;
export const authService = {
  setToken(t: string) { token = t; },
  getToken() { return token; },
  clearToken() { token = null; },
};