import { errorHandler, corsConf, headersConf } from "../utils/utils";

export default class DashboardApi {

  static create(widget) {
    return fetch('http://localhost:9000/api/widget', { method: 'POST', body: JSON.stringify(widget), headers: headersConf(), mode: 'cors' } )
      .then(errorHandler)
      .then(response => response.json().then((d) => d))
      .catch(error => { throw error });
  }

  static update(widget) {
    return fetch('http://localhost:9000/api/widget/'.concat(widget.id), { method: 'PUT', body: JSON.stringify(widget), headers: headersConf(), mode: 'cors' } )
      .then(errorHandler)
      .then(response => response.json().then((d) => d))
      .catch(error => { throw error });
  }

  static delete(widget) {
    return fetch('http://localhost:9000/api/widget/'.concat(widget.id), { method: 'DELETE', mode: 'cors' } )
      .then(errorHandler)
      .catch(error => { throw error });
  }

}
