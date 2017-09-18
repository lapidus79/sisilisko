import React from "react";
import PropTypes from "prop-types";
import {Col} from "react-bootstrap";
import PingWidget from "./PingWidget";

export default class Widget extends React.Component {

  render() {
    const {widget} = this.props;
    return (
      <Col xs={12} sm={4} md={3}>
        <PingWidget widget={widget}></PingWidget>
      </Col>
    );
  }

}

Widget.propTypes = {
  widget: PropTypes.object,
};

