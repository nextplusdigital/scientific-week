import store from '../store'

const needAuth = auth => auth === true

const beforeEach = (to, from, next) => {
  const auth = to.meta.requiresAuth

  /**
   * If route doesn't require authentication be normally accessed.
   */
  if (!needAuth(auth)) {
    next()
    return
  }

  /**
   * Otherwise  if authentication is required login.
   */
  store.dispatch('checkTokenUser')
    .then(() => {
      next()
    })
    .catch(() => {
      next({ name: 'auth' })
    })
}

export default beforeEach
