import { createBrowserRouter } from "react-router";
import { AppRoutes } from "./app.routes";
import AuthLayout from "@/layout/auth.layout";
import AppLayout from "@/layout/app.layout";
import { AuthRoutes } from "./auth.routes";
// import { Loader } from "lucide-react";

// const AppFallback = () => {
//   return (
//     <div className="h-screen w-full grid place-content-center">
//       <Loader />
//     </div>
//   )
// }

export const router = createBrowserRouter([
    {
      element: <AuthLayout />,
      children: AuthRoutes,
      // HydrateFallback: <AppFallback />
    },
    {
      element: <AppLayout />,
      children: AppRoutes,
      // HydrateFallback: <AppFallback />
    }
])