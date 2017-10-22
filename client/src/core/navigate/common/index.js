import App from 'src/core/shared/layout/App'
import Home from 'src/core/shared/layout/Home'
export default [
  {
    path: '/ws',
    component: App,
    children: [
      {
        path: '',
        component: Home,
        name: 'ws.home'
      }
    ]
  }
]
