import React from "react";
import PropTypes from "prop-types";
import {Modal, Button} from "react-bootstrap";

export default class DeletePrompt extends React.Component {

  render() {
    const {show, title, hideFn, deleteFn} = this.props;
    return (
      <Modal show={show}>
        <Modal.Header>
          <Modal.Title>
            Delete <strong>{title}</strong>?
          </Modal.Title>
        </Modal.Header>
        <Modal.Footer>
          <Button onClick={hideFn}>No</Button>
          <Button bsStyle="danger" onClick={deleteFn}>Yes</Button>
        </Modal.Footer>
      </Modal>
    );
  }

}

DeletePrompt.propTypes = {
  show: PropTypes.bool.isRequired,
  title: PropTypes.string,
  hideFn: PropTypes.func.isRequired,
  deleteFn: PropTypes.func.isRequired,
};
