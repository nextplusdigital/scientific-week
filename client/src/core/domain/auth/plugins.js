import { setItem } from 'src/core/helpers/storage'
import { setToken } from 'src/core/pluggables/http'
import * as TYPES from './mutations-types'

const subscribe = (store) => {
  store.subscribe((mutation, { auth }) => {
    if (TYPES.SET_TOKEN === mutation.type) {
      setToken(auth.token)
      setItem('token', auth.token, true)
    }
    if (TYPES.SET_USER === mutation.type) {
      setItem('user', auth.user, true)
    }
  })
}

export default (store) => {
  subscribe(store)
}
