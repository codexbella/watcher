export interface AuthInterface {
   token : string,
   username : string,
   login : (username: string, password: string) => Promise<void>,
   logout: () => void
}
