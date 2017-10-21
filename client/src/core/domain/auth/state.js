import { getItem } from 'src/core/helpers/storage'

const user = getItem('user')
const token = getItem('token')

export default {
  user: user || {},
  token: token || '',
  permissions: []
}
