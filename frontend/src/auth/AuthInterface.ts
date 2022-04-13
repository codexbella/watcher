export interface AuthInterface {
   token : string,
   username : string,
   expiration: boolean,
   login : (username: string, password: string) => Promise<void>,
   logout: () => void
}
