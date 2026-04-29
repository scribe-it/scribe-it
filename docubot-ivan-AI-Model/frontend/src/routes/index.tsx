import { Routes } from './paths'

export const AdminRoutes = [
  {
    path: Routes.home,
    async lazy() {
      const module = await import('@/pages/home')
      return { Component: module.default }
    },
  }
]
