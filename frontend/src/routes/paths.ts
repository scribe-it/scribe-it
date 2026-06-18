export const Routes = {
    home: '/',
    logIn: '/log-in',
    signUp: '/register-user',
    editor: '/editor',
    chats: '/chats',
    drafts: '/drafts',
    publications: '/publicaciones',
    chatDetail: (chatId:string) => `/chat/${chatId}`,
    profile: '/perfil',
    settings: '/settings',
};
