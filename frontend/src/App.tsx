import './App.css'
import { AuthProvider } from "./context/auth-provider";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { RouterProvider } from "react-router-dom";
import { router } from "./routes/router";
import { Toaster } from 'sonner';

const queryClient = new QueryClient();
  
function App() {
  return (
    <AuthProvider>
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
        <Toaster 
          position='top-center'/>
      </QueryClientProvider>
    </AuthProvider>
  );
}

export default App
