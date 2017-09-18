export default function streams(state = [], action) {

  switch (action.type) {

    case 'STREAM_DASHBOARD_UPDATE':
      if (state.some((d) => sameId(d.id, action.dashboard.id))) {
        return state.map((d) => {
          return (sameId(d.id, action.dashboard.id)) ?
            {...d, ...action.dashboard} :
            d;
        });
      } else {
        return [...state, action.dashboard];
      }

    default:
      return state;
  }

  function sameId(id, id2) {
    return (Number(id) === Number(id2));
  }

}
