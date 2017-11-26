module.exports = {
  NODE_ENV: '"production"',
  LOCALE: '"pt_BR"',
  APP: {
    NAME: '"Sistema de Vacinação"',
    DEVICE: '"appDevice"',
    TOKEN: '"appAuth"',
    USER: '"appUser"',
    REMEMBER: '"appRemember"',
  },
  API: {
    PROTOCOL: '"http"',
    DOMAIN: '"localhost"',
    PATH: '""',
    PORT: '"8080"'
  },
  OAUTH: {
    FACEBOOK: '""'
  },
  ROUTES: {
    LOGIN: {
      name: "'auth.login'"
    },
    NO_ACCESS: {
      name: "'dashboard.no-access'"
    },
    HOME: {
      name: "'dashboard.home'"
    }
  },
  DEV: false
}
