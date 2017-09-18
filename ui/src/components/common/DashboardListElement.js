import React from "react";
import PropTypes from "prop-types";
import {Link} from "react-router";

export default class DashboardListElement extends React.Component {

  render() {
    const {dashboard, showDelete} = this.props;
    return (
      <tr>
        <td>#{dashboard.id}</td>
        <td>{dashboard.name}</td>
        <td>{dashboard.tokenId}</td>
        <td style={{textAlign: 'right'}}>
          <Link to={'d/' + dashboard.tokenId}>Open</Link>&nbsp;&nbsp;&nbsp;
          <Link to={'ui/dashboard/edit/' + dashboard.id}>Edit </Link>&nbsp;&nbsp;&nbsp;
          <Link onClick={() => showDelete(dashboard)}>Delete</Link>
        </td>
      </tr>
    );
  }

}

DashboardListElement.propTypes = {
  dashboard: PropTypes.object.isRequired,
  showDelete: PropTypes.func.isRequired,
};
