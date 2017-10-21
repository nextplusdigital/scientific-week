import { postLogin } from 'services/auth'
import { isEmpty } from 'lodash'
import * as TYPES from './mutations-types'
import { setItem, getItem } from 'core/helpers/storage'

/**
 * @param dispatch
 * @param user
 */
export const doLogin = ({ dispatch }, user) => {
  return postLogin(user.username, user.password)
    .then((data) => {
      dispatch('setUser', data.info)
      dispatch('setToken', data.info.token)
      return user
    }).catch(erro => Promise.reject(erro))
}

/**
 * @param dispatch
 */
export const doLogout = ({ dispatch }) => {
  return Promise.all([
    dispatch('setUser', {}),
    dispatch('setToken', '')
  ]).then((data) => {
    return Promise.resolve(data)
  }).catch((err) => {
    Promise.reject(err)
  })
}

/**
 * @param commit
 * @param accessToken
 */
export const setToken = ({ commit }, accessToken) => {
  const token = (isEmpty(accessToken)) ? null : accessToken || accessToken.token
  commit(TYPES.SET_TOKEN, token)
  setItem('token', token, true)
  return Promise.resolve(token)
}

export const setUser = ({ commit }, user) => {
  commit(TYPES.SET_USER, user)
  setItem('user', user, true)
  return Promise.resolve(user)
}

/**
 * delegate of responsability checked of token
 * @param dispatch
 */
export const checkTokenUser = ({ dispatch, state }) => {
  if (!isEmpty(state.token)) {
    return Promise.resolve(state.token)
  }
  const token = getItem('token', true)
  const user = getItem('user', true)
  if (isEmpty(token)) {
    return Promise.reject(token)
  }
  dispatch('setToken', token)
  dispatch('setUser', user)
}
