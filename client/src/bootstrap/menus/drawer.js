import { menu } from 'genesis/modules/dashboard'

import { menu as home } from 'src/app/modules/dashboard/model/home'
import { menu as user } from 'src/domains/admin/user/model'
import { menu as remedio } from 'src/domains/admin/remedio/model'
import { menu as tipoanimal } from 'src/domains/admin/tipoanimal/model'
import { menu as animal } from 'src/domains/admin/animal/model'
import { menu as vacinacao } from 'src/domains/admin/vacinacao/model'
import { menu as vacinacaoanimal } from 'src/domains/admin/vacinacaoanimal/model'

export default (to) => [
  home(to),
  menu('admin', 'Cadastros', 'format_quote', [
    user(to),
    remedio(to),
    tipoanimal(to),
    animal(to)
  ]),
  vacinacao(to),
  vacinacaoanimal(to)
]
