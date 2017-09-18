import React from "react";
import {connect} from "react-redux";
import {Table} from "react-bootstrap";
import DashboardListElement from "../../components/common/DashboardListElement";
import DeletePrompt from "../../components/common/DeletePrompt";

export class DashboardList extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      delete_show: false,
      delete_dashboard: {},
    };

    this.showDeleteDashboardPrompt = this.showDeleteDashboardPrompt.bind(this);
    this.hideDeleteDashboardPrompt = this.hideDeleteDashboardPrompt.bind(this);
    this.deleteDashboard = this.deleteDashboard.bind(this);
  }

  render() {
    const {dashboards} = this.props;
    const dashboardList = dashboards.map((d, i) => {
      return (
        <DashboardListElement key={i} dashboard={d}
                              showDelete={this.showDeleteDashboardPrompt}/>
      );
    });
    return (
      <div>
        <Table hover responsive striped>
          <tbody>
            {dashboardList}
          </tbody>
        </Table>
        <DeletePrompt show={this.state.delete_show}
                      title={this.state.delete_dashboard.name}
                      hideFn={this.hideDeleteDashboardPrompt}
                      deleteFn={this.deleteDashboard}/>
      </div>
    );
  }

  showDeleteDashboardPrompt(dashboard) {
    this.setState({delete_show: true, delete_dashboard: dashboard});
  }

  hideDeleteDashboardPrompt() {
    this.setState({delete_show: false, delete_dashboard: {}});
  }

  deleteDashboard() {
    this.props.dispatch({type: 'DASHBOARD_DELETE', dashboard: this.state.delete_dashboard});
    this.hideDeleteDashboardPrompt();
  }

}

function mapStateToProps(state) {
  return {
    dashboards: state.dashboards,
  };
}
export default connect(mapStateToProps)(DashboardList);

