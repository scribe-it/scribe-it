import './App.css'
import { AuthProvider } from "./context/auth-provider";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { RouterProvider } from "react-router-dom";
import { router } from "./routes/router";

const queryClient = new QueryClient();
  
function App() {
  return (
    <AuthProvider>
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
      </QueryClientProvider>
    </AuthProvider>
  );
}

export default App
