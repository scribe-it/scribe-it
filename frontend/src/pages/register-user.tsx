import { Chat } from "@/components/secondary-sidebar"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"

type RegisterUserForm = {
    firstName: string
    lastName: string
    email: string
    password: string
    department: string
}

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
        <div className="flex items-center justify-center min-h-screen bg-gray-50">
            <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4 w-96 p-8 bg-white rounded-xl shadow" noValidate>
                <h1 className="text-2xl font-bold mb-2">Registrar Usuario</h1>

                {message && <p className="text-green-600 text-sm bg-green-50 p-2 rounded">{message}</p>}
                {error && <p className="text-red-600 text-sm bg-red-50 p-2 rounded">{error}</p>}

                <input
                    className="border rounded px-3 py-2"
                    placeholder="Nombre"
                    {...register("firstName", {
                        required: "El nombre es obligatorio",
                        minLength: { value: 2, message: "Debe tener al menos 2 caracteres" },
                    })}
                />
                {errors.firstName && <p className="text-red-600 text-xs">{errors.firstName.message}</p>}

                <input
                    className="border rounded px-3 py-2"
                    placeholder="Apellido"
                    {...register("lastName", {
                        required: "El apellido es obligatorio",
                        minLength: { value: 2, message: "Debe tener al menos 2 caracteres" },
                    })}
                />
                {errors.lastName && <p className="text-red-600 text-xs">{errors.lastName.message}</p>}

                <input
                    className="border rounded px-3 py-2"
                    type="email"
                    placeholder="Email"
                    {...register("email", {
                        required: "El email es obligatorio",
                        pattern: {
                            value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                            message: "Ingresa un email valido",
                        },
                    })}
                />
                {errors.email && <p className="text-red-600 text-xs">{errors.email.message}</p>}

                <select
                    className="border rounded px-3 py-2"
                    {...register("department", {
                        required: "Selecciona un departamento",
                    })}
                >
                    <option value="" disabled>Seleccione un departamento</option>
                    {departments.map((d) => (
                        <option key={d} value={d}>{d}</option>
                    ))}
                </select>
                {errors.department && <p className="text-red-600 text-xs">{errors.department.message}</p>}

                <input
                    className="border rounded px-3 py-2"
                    type="password"
                    placeholder="Contraseña"
                    {...register("password", {
                        required: "La contrasena es obligatoria",
                        minLength: { value: 8, message: "Debe tener al menos 8 caracteres" },
                    })}
                />
                {errors.password && <p className="text-red-600 text-xs">{errors.password.message}</p>}

                <button className="bg-blue-500 text-white rounded py-2 font-medium hover:bg-blue-600 disabled:opacity-70" type="submit" disabled={isSubmitting}>
                    {isSubmitting ? "Registrando..." : "Registrar"}
                </button>
            </form>
        </div>
    )
}
