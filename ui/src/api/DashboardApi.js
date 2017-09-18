import { errorHandler, corsConf, headersConf } from "../utils/utils";

export default class DashboardApi {

  static list() {
    return fetch('http://localhost:9000/api/dashboard/list', { method: 'GET', mode: 'cors' } )
      .then(errorHandler)
      .then(response => response.json().then((dashboards) => dashboards))
      .catch(error => { throw error });
  }

  static create(dashboard) {
    return fetch('http://localhost:9000/api/dashboard', { method: 'POST', body: JSON.stringify(dashboard), headers: headersConf(), mode: 'cors' } )
      .then(errorHandler)
      .then(response => response.json().then((d) => d))
      .catch(error => { throw error });
  }

  static update(dashboard) {
    return fetch('http://localhost:9000/api/dashboard/'.concat(dashboard.id), { method: 'PUT', body: JSON.stringify(dashboard), headers: headersConf(), mode: 'cors' } )
      .then(errorHandler)
      .then(response => response.json().then((d) => d))
      .catch(error => { throw error });
  }

  static delete(dashboard) {
    return fetch('http://localhost:9000/api/dashboard/'.concat(dashboard.id), { method: 'DELETE', mode: 'cors' } )
      .then(errorHandler)
      .catch(error => { throw error });
  }

}
