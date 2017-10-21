import { URL_API } from 'src/core/helpers/environment'
import axios from 'axios'

export const http = axios.create({
  baseURL: URL_API
})

export function setToken (token) {
  http.defaults.headers.common['Authorization'] = token
}

export default function install (Vue) {
  Object.defineProperty(Vue, '$http', {
    get: () => http
  })
}
