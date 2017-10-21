import { set } from 'src/support/storage'
import { setToken } from 'pluggables/http'
import * as TYPES from './mutations-types'

const subscribe = (store) => {
  store.subscribe((mutation, { auth }) => {
    if (TYPES.SET_TOKEN === mutation.type) {
      setToken(auth.token)
      set('token', auth.token, true)
    }
    if (TYPES.SET_USER === mutation.type) {
      set('user', auth.user, true)
    }
  })
}

export default (store) => {
  subscribe(store)
}
