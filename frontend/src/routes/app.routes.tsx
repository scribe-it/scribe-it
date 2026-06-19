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
  },
  {
    path: Routes.drafts,
    async lazy() {
      const module = await import('@/pages/draft')
      return { Component: module.default }
    }
  },
  {
    path: Routes.publications,
    async lazy() {
      const module = await import('@/pages/publications')
      return { Component: module.default }
    }
  },
  {
    path: Routes.users,
    async lazy() {
      const module = await import('@/pages/manage-users')
      return { Component: module.default }
    }
  },
  {
    path: Routes.profile,
    async lazy() {
      const module = await import('@/pages/profile')
      return { Component: module.default }
    }
  }
]
