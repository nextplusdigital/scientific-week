import { router as auth } from './auth'
import { default as common } from './common'
import { error } from './error'

export default [ ...auth, ...common, ...error ]
