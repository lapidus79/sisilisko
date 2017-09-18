import deepAssert from "assert";
import {assert} from 'chai';
import streamingWidgets from "../../src/reducers/streamingWidgets";

describe('streamingWidgets.reducer()', () => {

  let mockState = [
    //buildWidget(id, dashboardId, name, type, status, interval, url, statusCode, timestamp, latency)
    buildWidget(11, 1, "DASHTOKENID", "pingOne", "PING", "ACTIVE", 15, "http://blah", 200, 1503937087147, 110),
    buildWidget(12, 1, "DASHTOKENID", "pingTwo", "PING", "ACTIVE", 15, "http://blah", 400, 1503937087199, 120),
  ];

  describe('init()', () => {
    it('should return empty array', () => {
      deepAssert.deepEqual(streamingWidgets(undefined, {}), []);
    })
  });

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

  function buildWidget(id, dashboardId, dashboardTokenId, name, type, status, interval, url, statusCode, timestamp, latency) {
    let obj = {};

    obj.id = id;
    obj.dashboardId = dashboardId;
    obj.dashboardTokenId = dashboardTokenId;
    obj.name = name;
    obj.type = type;
    obj.status = status;

    obj.interval = interval;
    obj.url = url;
    obj.statusCode = statusCode;
    obj.timestamp = timestamp;
    obj.latency = latency;

    return obj;
  }

});
