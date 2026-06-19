import { Chat } from "@/components"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { z } from "zod"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"

const schema = z.object({
    firstName: z.string().min(2, "El nombre debe tener al menos 2 caracteres"),
    lastName: z.string().min(2, "El apellido debe tener al menos 2 caracteres"),
    email: z.string().email("Ingresa un email valido"),
    password: z.string().min(8, "La contrasena debe tener al menos 8 caracteres"),
    department: z.string().min(1, "Selecciona un departamento"),
})

type RegisterUserForm = z.infer<typeof schema>

export default function RegisterUser() {
    const [departments, setDepartments] = useState<string[]>([])
    const [message, setMessage] = useState("")
    const [error, setError] = useState("")
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting },
    } = useForm<RegisterUserForm>({
        mode: "onBlur",
        resolver: zodResolver(schema),
        defaultValues: {
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            department: "",
        },
    })

    useEffect(() => {
        const fetchData = async () => {
            const res = await fetch("/api/v1/chat", {
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            })
            if (!res.ok) {
                throw new Error("Error fetching roles")
            }
            const data = await res.json()
            setDepartments(data.map((d: Chat) => d.department))
        }
        fetchData()
    }, [])

    const onSubmit = async (formData: RegisterUserForm) => {
        setMessage("")
        setError("")

        try {
            const res = await fetch("/api/v1/admin", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify({
                    firstName: formData.firstName,
                    lastName: formData.lastName,
                    email: formData.email,
                    password: formData.password,
                    department: formData.department,
                    roleId: [4],
                }),
            })

            if (!res.ok) {
                const body = await res.text()
                throw new Error(body || "Error al crear usuario")
            }

            setMessage("Usuario registrado correctamente")
            reset()
        } catch (err: unknown) {
            if (err instanceof Error) {
                setError(err.message)
                return
            }
            setError("Error al registrar usuario")
        }
    }

    return (
        <div className="p-6 w-full flex items-center justify-center min-h-screen bg-background">
            <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4 w-96 p-8 bg-card rounded-xl shadow-panel" noValidate>
                <h1 className="text-2xl font-bold mb-2 text-white">Registrar usuario</h1>

                {message && <p className="text-teal-600 text-sm bg-teal-50 p-2 rounded">{message}</p>}
                {error && <p className="text-destructive text-sm bg-destructive/10 p-2 rounded">{error}</p>}

                <Input
                    placeholder="Nombre"
                    className="text-white"
                    {...register("firstName")}
                />
                {errors.firstName && <p className="text-red-600 text-xs">{errors.firstName.message}</p>}

                <Input
                    placeholder="Apellido"
                    className="text-white"
                    {...register("lastName")}
                />
                {errors.lastName && <p className="text-red-600 text-xs">{errors.lastName.message}</p>}

                <Input
                    type="email"
                    placeholder="Email"
                    className="text-white"
                    {...register("email")}
                />
                {errors.email && <p className="text-red-600 text-xs">{errors.email.message}</p>}

                <select
                    className="border border-border rounded-lg px-3 py-2 bg-background text-foreground"
                    {...register("department")}
                >
                    <option value="" disabled>Seleccione un departamento</option>
                    {departments.map((d) => (
                        <option key={d} value={d}>{d}</option>
                    ))}
                </select>
                {errors.department && <p className="text-red-600 text-xs">{errors.department.message}</p>}

                <Input
                    type="password"
                    placeholder="Contraseña"
                    className="text-white"
                    {...register("password")}
                />
                {errors.password && <p className="text-red-600 text-xs">{errors.password.message}</p>}

                <Button className="w-full" type="submit" disabled={isSubmitting}>
                    {isSubmitting ? "Registrando..." : "Registrar"}
                </Button>
            </form>
        </div>
    )
}
