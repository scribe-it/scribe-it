export const Routes = {
  home: '/',
  logIn: '/log-in',
  signUp: '/sign-up',
  editor: '/editor',
  chats: '/chats',
  chatDetail: (chatId: number) => `/chat/${chatId}`,
  profile: '/profile',
  settings: '/settings',
} as const
