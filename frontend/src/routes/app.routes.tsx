import { Routes } from './paths'

export const AppRoutes = [
  {
    path: Routes.home,
    async lazy() {
      const module = await import('@/pages/home')
      return { Component: module.default }
    },
  },
  {
    path: Routes.editor,
    async lazy() {
      const module = await import('@/pages/editor')
      return { Component: module.default }
    }
  }
]
