import deepAssert from "assert";
import {assert} from 'chai';
import dashboards from "../../src/reducers/dashboards";

describe('dashboards.reducer()', () => {

  describe('init()', () => {
    it('should return empty array', () => {
      deepAssert.deepEqual(dashboards(undefined, {}), []);
    })
  });

  /*
  describe('STREAM_WIDGET_UPDATE', () => {

    describe('when widget doesn\'t exist in tree', () => {

      let newWidget = buildWidget(13, 1, "PING", "pingOne", 15, 200, 1503937087147, 110);

      it('should contain the new widget obj', () => {
        const newState = streamingWidgets(mockState, {type: 'STREAM_WIDGET_UPDATE', widget: newWidget});
        assert.lengthOf(newState, mockState.length + 1);
        deepAssert.deepEqual(newState[2], newWidget);
      });

    });

    describe('when widget already exits in tree', () => {

      let updatedWidget = buildWidget(12, 1, "PING", "newWidgetName", 434, 3131, 43433, 312);

      it('should replace the existing widget obj', () => {
        const newState = streamingWidgets(mockState, {type: 'STREAM_WIDGET_UPDATE', widget: updatedWidget});
        assert.lengthOf(newState, mockState.length);
        deepAssert.deepEqual(newState[1], updatedWidget);
      });

    });

  });


  describe('STREAM_WIDGET_DELETE', () => {

    describe('when widget doesn\'t exist in tree', () => {

      let newWidget =  buildWidget(13, 1, "PING", "pingOne", 43, 300, 1503937087147, 110);

      it('should not change the tree', () => {
        const newState = streamingWidgets(mockState, {type: 'STREAM_WIDGET_DELETE', widget: newWidget});
        assert.lengthOf(newState, mockState.length);
        deepAssert.deepEqual(newState, mockState);
      });

    });

    describe('when widget exists in tree', () => {

      it('should remove the widget obj', () => {
        const newState = streamingWidgets(mockState, {type: 'STREAM_WIDGET_DELETE', widget: mockState[1]});
        assert.lengthOf(newState, mockState.length - 1);
        deepAssert.deepEqual(newState[0], mockState[0]);
      });

    });

  });

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

*/

});
