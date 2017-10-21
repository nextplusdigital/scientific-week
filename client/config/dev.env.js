var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  APP: {
    NAME: '"Semana de Tecnologia da Fameta"',
    DEVICE: '"appDevice"',
    TOKEN: '"appAuth"',
    USER: '"appUser"'
  },
  API: {
    PROTOCOL: '"http"',
    DOMAIN: '"localhost"',
    PATH: '"/api"',
    PORT: '"8080"'
  },
  DEV: true
})
