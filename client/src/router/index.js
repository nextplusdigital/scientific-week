import Vue from 'vue'
import VueRouter from 'vue-router'
import beforeEach from 'src/router/beforeEach'
import routes from 'src/router/routes'

Vue.use(VueRouter)

const router = new VueRouter({
  /*
   * NOTE! VueRouter "history" mode DOESN'T works for Cordova builds,
   * it is only to be used only for websites.
   *
   * If you decide to go with "history" mode, please also open /config/index.js
   * and set "build.publicPath" to something other than an empty string.
   * Example: '/' instead of current ''
   *
   * If switching back to default "hash" mode, don't forget to set the
   * build publicPath back to '' so Cordova builds work again.
   *
   * If load components use function below
   * function load (component) {
   * return () => System.import(`components/${component}.vue`)
   * }
   */
  routes,
  linkActiveClass: 'router-link-active'
})

router.beforeEach(beforeEach)

export default router
