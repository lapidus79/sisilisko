export default function streams(state = [], action) {

  switch (action.type) {

    case 'STREAM_WIDGET_UPDATE':
      if (state.some((d) => sameId(d.id, action.widget.id))) {
        return state.map((d) => {
          if (sameId(d.id, action.widget.id)) {
            return action.widget;
          } else {
            return d;
          }
        });
      } else {
        return [...state, action.widget];
      }

    default:
      return state;
  }

  function sameId(id, id2) {
    return (Number(id) === Number(id2));
  }

}

