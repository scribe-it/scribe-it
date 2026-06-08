export const Routes = {
  logIn: '/log-in',
  signUp: '/sign-up',
  home: '/home',
  editor: '/editor',
  chats: '/chats',
  chatDetail: (chatId: number) => `/chat/${chatId}`,
  profile: '/profile',
  settings: '/settings',
} as const
