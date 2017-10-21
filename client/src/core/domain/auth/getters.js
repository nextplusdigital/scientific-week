import { isEmpty, capitalize } from 'lodash'

export const isLogged = ({ token }) => !isEmpty(token)

export const getUser = ({ user }) => capitalize(user.login)

export const getToken = ({ token }) => token

export const getImageUser = ({ user }) => user.photo
