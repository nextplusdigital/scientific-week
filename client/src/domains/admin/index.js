import { meta } from 'genesis/support/model'
import { route } from 'genesis/infra/router/resources'

import user from 'src/domains/admin/user/routes'
import permission from 'src/domains/admin/permission/routes'
import organization from 'src/domains/admin/organization/routes'
import remedio from 'src/domains/admin/remedio/routes'
import tipoanimal from 'src/domains/admin/tipoanimal/routes'
import animal from 'src/domains/admin/animal/routes'
import vacinacao from 'src/domains/admin/vacinacao/routes'
import vacinacaoanimal from 'src/domains/admin/vacinacaoanimal/routes'

export const adminPath = '/dashboard/admin'
export const adminName = 'admin.index'
export const adminComponent = 'app/modules/dashboard/components/DashboardRouterView'
export const adminMeta = Object.assign(
  {}, {noLink: true}, meta('format_quote', 'Cadastros', 'Formulários Avançados')
)

export const routes = [
  route(adminPath, adminName, adminComponent, {}, adminMeta, [
    ...user,
    ...permission,
    ...organization,
    ...remedio,
    ...tipoanimal,
    ...animal,
    ...vacinacao,
    ...vacinacaoanimal
  ])
]
