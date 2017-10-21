// quasar wrappers
import { LocalStorage, SessionStorage } from 'quasar-framework'

/**
 * @param key
 * @param value
 * @param keep
 */
export const setItem = (key, value, keep) => {
  if (keep) {
    return LocalStorage.set(key, value)
  }
  return SessionStorage.set(key, value)
}

/**
 * @param key
 * @param keep
 */
export const getItem = (key, keep) => {
  if (keep) {
    return LocalStorage.get.item(key)
  }
  return SessionStorage.get.item(key)
}

/**
 * @param key
 * @param keep
 */
export const eraseItem = (key, keep) => {
  if (keep) {
    return LocalStorage.remove(key)
  }
  return SessionStorage.remove(key)
}
