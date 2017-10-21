/**
 * Vuex actions
 */
import * as types from './types'

/**
 * @param context
 * @param payload
 */
export const changeTitle = ({ commit }, payload) => {
  commit(types.setAppTitle, payload)
}

/**
 * @param context
 * @param payload
 */
export const changeMenu = ({ commit }, payload) => {
  commit(types.setAppMenu, payload)
}

/**
 * @param context
 * @param payload
 */
export const changeUser = ({ commit }, payload) => {
  commit(types.setAppUser, payload)
}
