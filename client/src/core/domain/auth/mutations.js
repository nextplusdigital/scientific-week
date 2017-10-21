import * as TYPES from './mutations-types'

export default {
  [TYPES.SET_USER] (state, user) {
    state.user = user
  },
  [TYPES.SET_TOKEN] (state, token) {
    state.token = token
  }
}
