import App from 'src/core/shared/layout/App'
import Home from 'src/core/shared/layout/Home'
export default [
  {
    path: '/dashboard',
    component: App,
    children: [
      {
        path: '',
        component: Home,
        name: 'dashboard.home'
      }
    ]
  }
]
