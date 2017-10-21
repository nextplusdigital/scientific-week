import state from './state'
import * as getters from './getters'
import * as actions from './actions'
import mutations from './mutations'
import plugins from './plugins'

const module = { actions, getters, mutations, state }

export default { module, plugins }
