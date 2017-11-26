var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  API: {
    PROTOCOL: '"http"',
    DOMAIN: '"192.168.0.12"',
    PATH: '""',
    PORT: '"8080"'
  },
  DEV: true
})
