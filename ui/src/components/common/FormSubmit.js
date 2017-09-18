import React from "react";
import PropTypes from "prop-types";
import { FormGroup, HelpBlock, Button } from "react-bootstrap";

export default class FormSubmit extends React.Component {

  render() {
    const {error, invalid, submitting, buttonSaveLoading, buttonSave} = this.props;
    let errorMsg = error ?
      <FormGroup validationState="error"><HelpBlock>{error}</HelpBlock></FormGroup> :
      null;

    return (
      <div>
        {errorMsg}
        <FormGroup className="submit">
          <Button type="submit" bsStyle="default" disabled={invalid || submitting}>
            {submitting ?
              (buttonSaveLoading ? buttonSaveLoading : 'Saving...') :
              (buttonSave ? buttonSave : 'Save')}
          </Button>
        </FormGroup>
      </div>
    );
  }
}

FormSubmit.propTypes = {
  error: PropTypes.string,
  invalid: PropTypes.bool,
  submitting: PropTypes.bool,
  buttonSaveLoading: PropTypes.string,
  buttonSave: PropTypes.string,
};
