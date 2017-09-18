import deepAssert from "assert";
import {assert} from 'chai';
import streamingDashboards from "../../src/reducers/streamingDashboards";

describe('streamingDashboards.reducer()', () => {

  let mockState = [
    buildDashboard(1, "abcdef", "dbOne", "ACTIVE"),
    buildDashboard(2, "ghijkl", "dbTwo", "ACTIVE"),
  ];

  describe('init()', () => {
    it('should return empty array', () => {
      deepAssert.deepEqual(streamingDashboards(undefined, {}), []);
    })
  });

  describe('STREAM_DASHBOARD_UPDATE', () => {

    describe('when dashboard doesn\'t exist in tree', () => {
      let newBoard = buildDashboard(99, "abcdef", "dbOne", "active");
      it('should append the new dashboard obj', () => {
        const newState = streamingDashboards(mockState, {type: 'STREAM_DASHBOARD_UPDATE', dashboard: newBoard});
        assert.lengthOf(newState, mockState.length + 1);
        deepAssert.deepEqual(newState[2], newBoard);
      });
    });

    describe('when dashboard already exits in tree', () => {
      let updatedBoard = buildDashboard(2, "ghijkl", "updatedName", "deleted");
      it('should replace the existing dashboard obj', () => {
        const newState = streamingDashboards(mockState, {type: 'STREAM_DASHBOARD_UPDATE', dashboard: updatedBoard});
        assert.lengthOf(newState, mockState.length);
        deepAssert.deepEqual(newState[1], updatedBoard);
      });
    });

  });

  function buildDashboard(id, tokenId, name, status) {
    let obj = {};
    obj.id = id;
    obj.tokenId = tokenId;
    obj.name = name;
    obj.status = status;
    return obj;
  }

});
