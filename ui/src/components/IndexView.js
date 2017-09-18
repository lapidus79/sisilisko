import React from "react";
import {PageHeader} from "react-bootstrap";
import {Link} from "react-router";
import DashboardList from "../components/common/DashboardList";

export default class IndexView extends React.Component {

  render() {
    return (
      <div>
        <PageHeader>
          Boards <Link to="/ui/dashboard/edit" className="header-link">Add</Link>
        </PageHeader>
        <DashboardList/>
      </div>
    );
  }

}
