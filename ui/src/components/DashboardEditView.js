import React from "react";
import { connect } from "react-redux";
import { push } from "react-router-redux";
import { Field, SubmissionError, reduxForm } from "redux-form";
import { PageHeader, Form, Row } from "react-bootstrap";
import FormField from "./common/FormField";
import FormSubmit from "./common/FormSubmit";

export class DashboardEditView extends React.Component {

  constructor(props) {
    super(props);
    this.formSubmit = this.formSubmit.bind(this);
  }

  render() {
    const {dashboard, handleSubmit, error, invalid, submitting} = this.props;
    return (
      <div className="page-user-edit">
        <PageHeader>{ (dashboard.id ? 'Edit' : 'Add') + ' dashboard' }</PageHeader>
        <div>
          <Form onSubmit={handleSubmit(this.formSubmit)}>
            <Field component={FormField} name="name" label="Dashboard name" doValidate={true}/>
            <FormSubmit error={error} invalid={invalid} submitting={submitting} buttonSaveLoading="Saving..." buttonSave="Save"/>
          </Form>
        </div>
      </div>
    );
  }

  formSubmit(values) {
    const {dispatch, dashboard} = this.props;
    return new Promise((resolve, reject) => {
      dispatch({
        type: (dashboard.id) ? 'DASHBOARD_UPDATE' : 'DASHBOARD_CREATE',
        dashboard: { name: values.name, id: dashboard.id },
        callbackError: (error) => { reject(new SubmissionError({_error: error})); },
        callbackSuccess: () => { dispatch(push('/')); resolve(); }
      });
    });
  }

}

const DashboardEditForm = reduxForm({
  form: 'dashboard_edit',
  validate: function (values) {
    const errors = {};
    if (!values.name) {
      errors.name = 'Name is required';
    }
    return errors;
  },
})(DashboardEditView);

function mapStateToProps(state, own_props) {
  const dashboard = state.dashboards.find(x => Number(x.id) === Number(own_props.params.id)) || {};
  return {
    dashboard: dashboard,
    initialValues: dashboard,
  };
}
export default connect(mapStateToProps)(DashboardEditForm);
