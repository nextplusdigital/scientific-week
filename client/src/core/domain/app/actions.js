/**
 * Vuex actions
 */
import * as types from './types'

/**
 * @param context
 * @param payload
 */
export const changeTitle = (context, payload) => {
  context.commit(types.setAppTitle, payload)
}

/**
 * @param context
 * @param payload
 */
export const changeMenu = (context, payload) => {
  context.commit(types.setAppMenu, payload)
}

/**
 * @param context
 * @param payload
 */
export const changeUser = (context, payload) => {
  context.commit(types.setAppUser, payload)
}
