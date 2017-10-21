import { APP_NAME, APP_USER } from 'src/core/helpers/environment'
import { getItem } from 'src/core/helpers/storage'

const label = APP_NAME

let user = getItem(APP_USER)

export default {
  app: {
    name: label,
    title: label,
    menu: [],
    user: user
  }
}
