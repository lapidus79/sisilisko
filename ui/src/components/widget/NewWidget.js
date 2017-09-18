import React from "react";
import PropTypes from "prop-types";
import {Link} from "react-router";
import {Glyphicon, Col} from "react-bootstrap";

export default class NewWidget extends React.Component {

  render() {
    const {dashboardId} = this.props;
    return (
      <Col xs={12} sm={4} md={3}>
        <section className="widget">
          <div style={{border: '1px dotted #D6D6D6', height: '100%', paddingTop: '33% ', textAlign: 'center', fontSize: '36px'}}>
            <Link to={"/ui/widget/edit/" + dashboardId}>
              <Glyphicon glyph="plus"/>
            </Link>
          </div>
        </section>
      </Col>
    );
  }

}

NewWidget.propTypes = {
  dashboardId: PropTypes.number
};
