import model from 'genesis/support/model'
import { resource, source } from 'genesis/infra/services/http/resource'
import { button } from 'genesis/modules/dashboard'
import 'src/domains/general/slots/MyLink'
import { PATH_HOME } from 'genesis/support/index'

/**
 * @type {string}
 */
export const icon = 'assignment_turned_in'

/**
 * @type {string}
 */
export const label = 'Vacinações'

/**
 * @type {string}
 */
export const title = 'Cadastro de Vacinação'

/**
 * @type {string}
 */
export const tooltip = 'Gerencie o organize o seu cadastro de Vacinação'

/**
 * @type {string}
 */
export const description = 'Lista de Vacinações'

/**
 * @type {string}
 */
export const api = '/api/vacinacaos'

/**
 * @type {string}
 */
export const id = 'id'

/**
 * @type {string}
 */
export const path = '/dashboard/admin/vacinacaos'

/**
 * @type {string}
 */
export const namespace = 'admin.vacinacaos'

/**
 * @type {Resource}
 */
export const service = resource(api)

export const pivot = model.pivot('/api/pessoas', {value: 'id', label: 'descricao'}, 'id')
/**
 * @type {Object}
 */
export const reference = {
  value: 'id',
  label: 'nome'
}

/**
 * @type {Object}
 */
export const remote = {
  service: service,
  reference: reference,
  query: ['descricao']
}

/**
 * @type {Object}
 */
export const meta = model.meta(icon, label, title, tooltip)

/**
 * @type {Function}
 */
export const menu = model.menu(icon, label, path, false, tooltip, 'admin.vacinacaos', namespace, 1)

/**
 * @type {Object}
 */
export const sources = source(api, 'id', 'descricao')

/**
 * @param {Vue} $this
 * @param {Array} actions
 */
const actions = ($this, actions) => {
  // const permission = (record, $component, $user) => record && String(record['id']) === '2'
  const permission = record => record && String(record['id']) === '1'

  const home = () => $this.$router.push(PATH_HOME)

  // id, permission, label, handler, icon = '', tooltip = '', color = 'white'
  const custom = button('go-home', 1, 'Início', home, 'store', 'Voltar para a Página Inicial', 'warning')
    .$options({permission, rotate: false, raised: true}).$form() // , round: true, outline: true

  actions.unshift(custom)

  return actions.map(button => {
    if (button.id === 'destroy') {
      // override the access control system
      button.permission = permission
    }
    return button
  })
}

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

  return model.grid(service, path, id, fields('index', route), filters(scope, route), actions, options)
}

/**
 * @param {string} scope
 * @param {Route} route
 * @returns {Object}
 */
export const form = (scope, route) => {
  return model.form(service, scope, path, id, fields(scope, route), actions)
}

/**
 * @param {string} scope
 * @param {Route} route
 * @returns {Array}
 */
export const fields = (scope, route = null) => {
  return model.filter(
    [
      model.field('id', 'Código').$pk().$render(),
      model.field('nome', 'Nome').$filter().$required().$render(),
      model.field('date', 'Data').$form({width: 30}).$date().$required().$render(),
      model.field('login', 'Responsável').$form({width: 30}).$render(),
      model.field('login', 'Responsável').$out('index')
        .$form({width: 100, placeholder: '.: Selecione o Responsável :.'})
        .$pivot(pivot).$render()
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
