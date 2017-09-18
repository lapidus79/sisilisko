export default function dashboards(state = { title: 'SISILISKO' }, action) {

  switch (action.type) {

    case 'NAVBAR_TITLE':
      return {title: action.title};

    case "@@router/LOCATION_CHANGE":
      return { title: 'SISILISKO' };

    default:
      return state;

  }

}