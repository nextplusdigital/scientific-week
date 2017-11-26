import http from 'src/app/infra/services/http'
import store from 'src/app/infra/store'

/**
 * @param {string} user
 * @param {string} password
 * @param {function} success
 * @param {function} fail
 */
export const login = (user, password, success, fail) => {
  const credentials = {}
  credentials['username'] = user
  credentials['password'] = password
  http
    .post('/api/authenticate', credentials)
    .then(({data}) => {
      store
        .dispatch('login', {
          user: data.username,
          token: data.id_token
        })
        .then(() => {
          success(data)
        })
    })
    .catch(fail)
}
