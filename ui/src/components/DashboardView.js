import React from "react";
import {connect} from "react-redux";
import {Row,Col} from "react-bootstrap";
import Widget from "../components/widget/Widget";
import NewWidget from "../components/widget/NewWidget";

export class DashboardView extends React.Component {

  constructor(props) {
    super(props);
  }

  componentWillMount() {
    let newName = (this.props.dashboard) ? this.props.dashboard.name : "Loading dashboard...";
    this.props.dispatch({ type: 'NAVBAR_TITLE', title: newName });
    setTimeout(() => {
      this.props.dispatch({type: 'WS_SEND_MESSAGE', command: "SUBSCRIBE", payload: this.props.params.dashboardTokenId});
    }, 1000);
  }

  componentWillUnmount() {
    this.props.dispatch({type: 'WS_SEND_MESSAGE', command: "UNSUBSCRIBE", payload: this.props.params.dashboardTokenId});
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.dashboard) {
      if (this.props.dashboard) {
        if (this.props.dashboard.name !== nextProps.dashboard.name) {
          let newTitle = nextProps.dashboard.name || "";
          this.props.dispatch({ type: 'NAVBAR_TITLE', title: newTitle });
        }
      } else {
        let newTitle = nextProps.dashboard.name || "";
        this.props.dispatch({ type: 'NAVBAR_TITLE', title: newTitle });
      }
    }
  }

  render() {
    const { dashboard, widgets } = this.props;
    let widgetPane = null;
    let newWidget = null;

    if (dashboard) {
      newWidget = <NewWidget dashboardId={dashboard.id}/>;
    }
    if (dashboard && dashboard.status === 'ACTIVE') {
      widgetPane = widgets.map((w, i) => (<Widget key={i} widget={w} />));
    } else if (dashboard && dashboard.status === 'DELETED') {
      widgetPane = (<Col style={{textAlign: 'center', marginTop: '10vh'}} className="h1" xs={12} sm={12} lg={12} md={12}>This dashboard has been removed.</Col>);
      newWidget = null;
    } else {
      widgetPane = null; // loading
    }

    return (
      <div className="page-dashboard">
        <Row>
          { widgetPane }
          { newWidget }
        </Row>
      </div>
    );
  }

}

function mapStateToProps(state, own_params) {
  const dashboardTokenId = own_params.params.dashboardTokenId;
  const dashboard = state.streamingDashboards.find((d) => d.tokenId === dashboardTokenId);
  return {
    dashboardTokenId: dashboardTokenId,
    dashboard: dashboard,
    widgets: (dashboard) ? state.streamingWidgets
        .filter((w) => w.dashboardId === dashboard.id)
        .filter((w) => w.status === 'ACTIVE') : [],
  };
}

export default connect(mapStateToProps)(DashboardView);


