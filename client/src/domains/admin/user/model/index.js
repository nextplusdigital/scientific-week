import model from 'genesis/support/model'
import { resource } from 'genesis/infra/services/http/resource'

/**
 * @type {string}
 */
export const icon = 'people'

/**
 * @type {string}
 */
export const label = 'Usuários'

/**
 * @type {string}
 */
export const title = 'Cadastro de Usuários'

/**
 * @type {string}
 */
export const tooltip = 'Defina quais usuários terão acesso a sua aplicação e gerencie-os'

/**
 * @type {string}
 */
export const description = 'Adicione e gerencie Usuários no sistema e defina como se comportam'

/**
 * @type {string}
 */
export const api = '/api/users'

/**
 * @type {string}
 */
export const id = 'id'

/**
 * @type {string}
 */
export const path = '/dashboard/admin/user'

/**
 * @type {string}
 */
export const namespace = 'admin.user'

/**
 * @type {Resource}
 */
export const service = resource(api)

/**
 * @type {Object}
 */
export const meta = model.meta(icon, label, title, tooltip)

/**
 * @type {Function}
 */
export const menu = model.menu(icon, label, path, false, tooltip, 'user', namespace, 1)

/**
 * @type {Array}
 */
const slots = []

/**
 * @param {string} scope
 * @param {Route} route
 * @returns {Object}
 */
export const grid = (scope, route) => {
  // you can add settings default to grid in src/bootstrap/configure/grid
  const options = {
    slots: slots,
    position: 'right',
    top: true,
    bottom: false,
    paginate: false,
    styles: {
      height: 'calc(100vh - 235px)',
      minHeight: '280px'
    },
    bodyStyle: {
      height: 'calc(100vh - 285px)',
      minHeight: '230px'
    },
    debug: true
  }

  return model.grid(service, path, id, fields('index', route), filters(scope, route), {}, options)
}

/**
 * @param {string} scope
 * @param {Route} route
 * @returns {Object}
 */
export const form = (scope, route) => {
  const options = {
    tab: 'principal',
    tabs: [
      {
        name: 'principal',
        label: 'Principal'
      },
      {
        name: 'outros',
        label: 'Outros'
      }
    ],
    debug: true
  }
  // service, scope, path, id, schemas, actions = null, options = {}
  return model.form(service, scope, path, id, fields(scope, route), null, options)
}

/**
 * @param {string} scope
 * @param {Route} route
 * @returns {Array}
 */
export const fields = (scope, route = null) => {
  return model.filter(
    [
      model.field('id', 'Código').$pk().$tab('principal').$render(),
      model.field('login', 'Login').$text().$tab('principal').$filter().$required().$render(),
      model.field('email', 'E-mail').$tab('principal').$text().$filter().$form({width: 50}).$render(),
      model.field('password', 'Senha').$tab('principal').$password().$required(scope === 'create')
        .$scopes(['create', 'edit']).$tab('principal').$form({width: 50}).$render(),
      model.field('authorities', 'Perfil').$tab('principal').$out('index').$form({width: 30})
        .$select(profiles, true).$render()
    ],
    scope
  )
}

/**
 * @param {string} scope
 * @param {Route} route
 * @returns {Array}
 */
export const filters = (scope, route = null) => {
  return []
}

/**
 * @type {Array}
 */
export const profiles = [
  {label: 'Administratdor', value: 'ROLE_ADMIN'},
  {label: 'Usuário', value: 'ROLE_USER'}
]
