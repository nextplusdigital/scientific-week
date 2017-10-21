import { get } from 'src/support/storage'

const user = get('user')
const token = get('token')

export default {
  user: user || {},
  token: token || '',
  permissions: []
}
