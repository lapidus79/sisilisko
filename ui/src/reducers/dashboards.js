export default function dashboards(state = [], action) {

  switch (action.type) {
    case 'DASHBOARD_LIST_SAVE':
      return action.dashboards;

    case 'DASHBOARD_CREATE_SAVE':
      return [...state, action.dashboard];

    case 'DASHBOARD_UPDATE_SAVE':
      return state.map(d => Number(d.id) === Number(action.dashboard.id) ? {...action.dashboard} : d);

    case 'DASHBOARD_DELETE_SAVE':
      return state.filter(d => Number(d.id) !== Number(action.dashboard.id));

    default:
      return state;
  }
}