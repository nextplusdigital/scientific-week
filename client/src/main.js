// === DEFAULT / CUSTOM STYLE ===
// WARNING! always comment out ONE of the two require() calls below.
// 1. use next line to activate CUSTOM STYLE (./src/themes)
require(`./themes/app.${__THEME}.styl`)
// 2. or, use next line to activate DEFAULT QUASAR STYLE
// require(`quasar/dist/quasar.${__THEME}.css`)
// ==============================

// Uncomment the following lines if you need IE11/Edge support
// require(`quasar/dist/quasar.ie`)
// require(`quasar/dist/quasar.ie.${__THEME}.css`)

import Vue from 'vue'
import Quasar from 'quasar'
import Router from './core/router'
import Store from './core/store'
import Http from './core/pluggables/http'

Vue.config.productionTip = false
Vue.use(Quasar) // Install Quasar Framework

Vue.use(Http) // Http inject dependecy

Vue.config.errorHandle = (err, vm, info) => {
  console.log(' err ' + JSON.stringify(err) + ' vm info ' + JSON.stringify(vm) + ' infoo error ' + JSON.stringify(info))
}

if (__THEME === 'mat') {
  require('quasar-extras/roboto-font')
}
// import 'quasar-extras/material-icons'
// import 'quasar-extras/ionicons'
import 'quasar-extras/fontawesome'
// import 'quasar-extras/animate'

/* eslint-disable no-new */
new Vue({
  el: '#q-app',
  Router,
  Store,
  render: h => h(require('./Root').default)
})
