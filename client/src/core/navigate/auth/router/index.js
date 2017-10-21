import Login from '../component/Login'

export default [
  {
    name: 'login', path: '/login', component: Login, meta: { requiresAuth: true }
  }
]
