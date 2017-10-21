import { Dialog } from 'quasar-framework'

/**
 *
 * @param title title of message
 * @param message message of content
 * @param handler despatch method callback
 */
export const confirm = (title, message, handler) => {
  Dialog.create({
    title,
    message,
    buttons: [
      {
        label: 'Não'
      },
      {
        label: 'Sim', handler}
    ]
  })
}
