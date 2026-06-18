import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useAuth } from "@/context/use-auth";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import { Chat } from "@/components";

type ProfileFormData = {
  firstName: string;
  lastName: string;
  username: string;
  department: string;
  password: string;
  confirmPassword: string;
};

const Profile = () => {
  const { userData } = useAuth();

  const [departments, setDepartments] = useState<string[]>([]);

  useEffect(() => {
    const fetchDepartments = async () => {
      const res = await fetch("/api/v1/chat", {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      if (res.ok) {
        const data: Chat[] = await res.json();
        setDepartments([...new Set(data.map((d) => d.department))]);
      }
    };
    fetchDepartments();
  }, []);

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<ProfileFormData>({
    defaultValues: {
      firstName: userData?.firstName ?? "",
      lastName: userData?.lastName ?? "",
      username: userData?.username ?? "",
      department: userData?.department ?? "",
      password: "",
      confirmPassword: "",
    },
  });

  const password = watch("password");

  const onSubmit = (formData: ProfileFormData) => {
    if (!userData) return;

    const updatedUser = {
      ...userData,
      firstName: formData.firstName.trim(),
      lastName: formData.lastName.trim(),
      username: formData.username.trim(),
      department: formData.department.trim(),
    };

    localStorage.setItem("userData", JSON.stringify(updatedUser));
    toast.success("Perfil actualizado en esta sesion");
    reset({ ...formData, password: "", confirmPassword: "" });
  };

  const handleReset = () => {
    reset({
      firstName: userData?.firstName ?? "",
      lastName: userData?.lastName ?? "",
      username: userData?.username ?? "",
      department: userData?.department ?? "",
      password: "",
      confirmPassword: "",
    });
  };

  return (
    <main className="flex w-full bg-background p-6">
      <section className="w-full space-y-4">
        <h1 className="text-2xl font-bold text-foreground">Perfil</h1>

        {!userData && (
          <Card>
            <CardContent className="pt-4 text-muted-foreground">
              No hay datos de usuario para mostrar.
            </CardContent>
          </Card>
        )}
      <div className="h-[80vh] w-full flex items-center justify-center">      
          {userData && (
            <Card>
              <CardHeader>
                <div className="flex items-center gap-3">
                  <Avatar className="size-12">
                    <AvatarFallback className="bg-primary text-primary-foreground text-base font-semibold">
                      {userData.firstName?.charAt(0).toUpperCase()}{userData.lastName?.charAt(0).toUpperCase()}
                    </AvatarFallback>
                  </Avatar>
                  <div className="flex-1">
                    <CardTitle className="text-lg">{userData.firstName} {userData.lastName}</CardTitle>
                    <p className="text-sm text-muted-foreground">{userData.username}</p>
                  </div>
                  <Badge variant="secondary">ID #{userData.id}</Badge>
                </div>
              </CardHeader>
              <CardContent>
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-1">
                      <label className="text-xs font-medium text-muted-foreground" htmlFor="firstName">
                        Nombre
                      </label>
                      <Input
                        id="firstName"
                        placeholder="Nombre"
                        {...register("firstName", {
                          required: "El nombre es obligatorio",
                        })}
                      />
                      {errors.firstName && (
                        <p className="text-xs text-red-400">{errors.firstName.message}</p>
                      )}
                    </div>
                    <div className="space-y-1">
                      <label className="text-xs font-medium text-muted-foreground" htmlFor="lastName">
                        Apellido
                      </label>
                      <Input
                        id="lastName"
                        placeholder="Apellido"
                        {...register("lastName", {
                          required: "El apellido es obligatorio",
                        })}
                      />
                      {errors.lastName && (
                        <p className="text-xs text-red-400">{errors.lastName.message}</p>
                      )}
                    </div>
                  </div>

                  <div className="space-y-1">
                    <label className="text-xs font-medium text-muted-foreground" htmlFor="username">
                      Nombre de usuario
                    </label>
                    <Input
                      id="username"
                      placeholder="Nombre de usuario"
                      {...register("username", {
                        required: "El nombre de usuario es obligatorio",
                        minLength: { value: 2, message: "Debe tener al menos 2 caracteres" },
                      })}
                    />
                    {errors.username && (
                      <p className="text-xs text-red-400">{errors.username.message}</p>
                    )}
                  </div>

                  <div className="space-y-1">
                    <label className="text-xs font-medium text-muted-foreground" htmlFor="department">
                      Departamento
                    </label>
                    <select
                      id="department"
                      className="flex h-8 w-full min-w-0 rounded-lg border border-input bg-transparent px-2.5 py-1 text-base transition-colors outline-none file:inline-flex file:h-6 file:border-0 file:bg-transparent file:text-sm file:font-medium file:text-foreground placeholder:text-muted-foreground focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50 disabled:pointer-events-none disabled:cursor-not-allowed disabled:bg-input/50 disabled:opacity-50 md:text-sm dark:bg-input/30 dark:disabled:bg-input/80"
                      {...register("department", {
                        required: "Selecciona un departamento",
                      })}
                    >
                      <option value="" disabled>Seleccione un departamento</option>
                      {departments.map((d) => (
                        <option key={d} value={d} className="bg-background">{d}</option>
                      ))}
                    </select>
                    {errors.department && (
                      <p className="text-xs text-red-400">{errors.department.message}</p>
                    )}
                  </div>

                  <div className="space-y-1">
                    <label className="text-xs font-medium text-muted-foreground" htmlFor="password">
                      Cambiar contraseña
                    </label>
                    <Input
                      id="password"
                      type="password"
                      placeholder="Nueva contraseña"
                      {...register("password", {
                        minLength: { value: 8, message: "Debe tener al menos 8 caracteres" },
                      })}
                    />
                    {errors.password && (
                      <p className="text-xs text-red-400">{errors.password.message}</p>
                    )}
                  </div>

                  <div className="space-y-1">
                    <label className="text-xs font-medium text-muted-foreground" htmlFor="confirmPassword">
                      Confirmar nueva contraseña
                    </label>
                    <Input
                      id="confirmPassword"
                      type="password"
                      placeholder="Confirmar nueva contraseña"
                      {...register("confirmPassword", {
                        validate: (value) =>
                          !value || value === password || "Las contrasenas no coinciden",
                      })}
                    />
                    {errors.confirmPassword && (
                      <p className="text-xs text-red-400">{errors.confirmPassword.message}</p>
                    )}
                  </div>

                  <div className="flex items-center justify-end gap-2 pt-2">
                    <Button type="button" variant="outline" onClick={handleReset}>
                      Cancelar
                    </Button>
                    <Button type="submit">Guardar cambios</Button>
                  </div>
                </form>
              </CardContent>
            </Card>
          )}
      </div>
      </section>
    </main>
  );
};

export default Profile;
