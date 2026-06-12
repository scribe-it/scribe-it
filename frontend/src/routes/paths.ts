export const Routes = {
    home: '/',
    logIn: '/log-in',
    signUp: '/register-user',
    editor: '/editor',
    chats: '/chats',
    publications: '/publicaciones',
    chatDetail: (chatId:string) => `/chat/${chatId}`,
    profile: '/profile',
    settings: '/settings',
};
