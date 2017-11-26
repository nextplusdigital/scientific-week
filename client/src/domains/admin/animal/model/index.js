import model from 'genesis/support/model'
import { resource, source } from 'genesis/infra/services/http/resource'
import { button } from 'genesis/modules/dashboard'
import 'src/domains/general/slots/MyLink'
import { PATH_HOME } from 'genesis/support/index'
import {
  reference as tipoanimalReference,
  api as tipoanimalApi,
  path as tipoanimalPath
} from 'src/domains/admin/tipoanimal/model'

/**
 * @type {string}
 */
export const icon = 'list'

/**
 * @type {string}
 */
export const label = 'Animais'

/**
 * @type {string}
 */
export const title = 'Cadastro de Animais'

/**
 * @type {string}
 */
export const tooltip = 'Gerencie o organize o seu cadastro de Animais'

/**
 * @type {string}
 */
export const description = 'Animais utilizados para vacinação'

/**
 * @type {string}
 */
export const api = '/api/animals'

/**
 * @type {string}
 */
export const id = 'id'

/**
 * @type {string}
 */
export const path = '/dashboard/admin/animais'

/**
 * @type {string}
 */
export const namespace = 'admin.animais'

/**
 * @type {Resource}
 */
export const service = resource(api)

const tipoanimalFk = 'id'

export const pivot = model.pivot(tipoanimalApi, tipoanimalReference, tipoanimalFk)

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
export const menu = model.menu(icon, label, path, false, tooltip, 'admin.animais', namespace, 1)

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
const slots = [
  {
    field: 'name',
    component: 'MyLink',
    props: {
      path: `${tipoanimalPath}/{${tipoanimalFk}}`
    }
  }
]

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
      model.field('descricao', 'Descrição').$filter().$required().$render(),
      model.field('tipoAnimal', 'Tipo Animal').$form({width: 30}).$render(),
      model.field('tipoAnimal', 'Tipo Animal').$out('index')
        .$form({width: 100, placeholder: '.: Selecione os Tipo Animal :.'})
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
