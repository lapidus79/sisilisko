import React from "react";
import { connect } from "react-redux";
import { push } from "react-router-redux";
import { Field, SubmissionError, reduxForm } from "redux-form";
import { PageHeader, Form, Row } from "react-bootstrap";
import FormField from "./common/FormField";
import FormSubmit from "./common/FormSubmit";

export class WidgetEditView extends React.Component {

  constructor(props) {
    super(props);
    this.formSubmit = this.formSubmit.bind(this);
  }

  render() {
    const {widget, dashboard, handleSubmit, error, invalid, submitting} = this.props;
    return (
      <div className="page-user-edit">
        <PageHeader>{ (widget ? 'Edit widget ' + widget.name + ' in ' : 'Add widget to') + '  ' + dashboard.name }</PageHeader>
        <div>

          <Form onSubmit={handleSubmit(this.formSubmit)}>
            <Field component={FormField} name="name" label="Name" doValidate={true}/>
            <Field component={FormField} name="url" label="Url" doValidate={true}/>
            <FormSubmit error={error} invalid={invalid} submitting={submitting} buttonSaveLoading="Saving..." buttonSave="Save"/>
          </Form>

        </div>
      </div>
    );
  }

  formSubmit(values) {
    const {dispatch, widget, dashboard} = this.props;
    return new Promise((resolve, reject) => {
      dispatch({
        type: (widget.id) ? 'WIDGET_UPDATE' : 'WIDGET_CREATE',
        widget: { name: values.name, conf: { url: values.url }, dashboardId: dashboard.id, type: 'PING', id: widget.id },
        callbackError: (error) => { reject(new SubmissionError({_error: error})); },
        callbackSuccess: () => { dispatch(push('/d/'+dashboard.tokenId)); resolve(); }
      });
    });
  }

}

const WidgetEditForm = reduxForm({
  form: 'widget_edit',
  validate: function (values) {
    const errors = {};
    if (!values.name) {
      errors.name = 'Name is required';
    }
    if (!/^(http|https):\/\/[^ "]+$/.test(values.url)) {
      errors.url = 'Url is required and must be of format http://xxx.yyy.zzz';
    }
    return errors;
  },
})(WidgetEditView);

function mapStateToProps(state, own_props) {
  const dashboardId = own_props.params.dashboardId;
  const widgetId = own_props.params.id;
  const widget = state.streamingWidgets.find(x => Number(x.id) === Number(widgetId)) || { };
  const dashboard = state.dashboards.find(x => Number(x.id) === Number(dashboardId)) || {};
  return {
    dashboard: dashboard,
    widget: widget,
    initialValues: widget
  };
}
export default connect(mapStateToProps)(WidgetEditForm);
