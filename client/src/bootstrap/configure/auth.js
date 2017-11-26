/**
 * @param {object} credentials
 * @returns {object}
 */
export const configureAuth = (credentials) => {
  return { username: credentials.user, password: credentials.password }
}

/**
 * @param {object} data ()
 * @returns {object}
 */
export const configureAuthData = { user: '', password: '' }
