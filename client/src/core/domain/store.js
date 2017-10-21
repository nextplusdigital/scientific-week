import auth from './auth'
import app from './app'
import { isFunction } from 'lodash'

const store = { auth, app }

const keys = Object.keys(store)

const modules = keys.reduce((acc, key) => ({ ...acc, [key]: store[key].module }), {})
const plugins = keys.reduce((acc, key) => [...acc, store[key].plugins], []).filter(isFunction)

export default { modules, plugins }
