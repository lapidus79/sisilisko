import React from "react";
import { shallow } from "enzyme";
import assert from "assert";
import NotFoundView from "../../src/components/NotFoundView";


describe('NotFoundView.component', () => {

  describe('render()', () => {
    it('should render the component', () => {
      const wrapper = shallow(<NotFoundView/>);
      assert.equal(wrapper.length, 1);
    });
  });

});
