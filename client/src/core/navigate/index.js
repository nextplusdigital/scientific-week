import { router as auth } from './auth'
import { default as common } from './common'
import { error } from './error'
const routes = [ ...auth, ...common, ...error ]
export default routes
