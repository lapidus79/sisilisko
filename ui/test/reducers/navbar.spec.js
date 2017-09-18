import deepAssert from "assert";
import navbar from "../../src/reducers/navbar";

describe('navbar.reducer()', () => {

  describe('init()', () => {
    it('should return default title', () => {
      deepAssert.deepEqual(navbar(undefined, {}), { title: "SISILISKO" });
    })
  });

  describe('@@router/LOCATION_CHANGE', () => {

    it('should set default title', () => {
      deepAssert.deepEqual(navbar( { title: "BLAH" },  {type: '@@router/LOCATION_CHANGE' }), { title: "SISILISKO" });
    });

  });

  describe('NAVBAR_TITLE', () => {

    it('should set default title', () => {
      deepAssert.deepEqual(navbar({ title: "SISILISKO" },  {type: 'NAVBAR_TITLE', title: 'newTitle' }), { title: "newTitle" });
    });

  });

});
