import { api } from 'core/helpers'
import axios from 'axios'

export const http = axios.create({
  baseURL: api
})

export default function install (Vue) {
  Object.defineProperty(Vue, '$http', {
    get: () => http
  })
}
