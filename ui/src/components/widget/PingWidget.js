import React from "react";
import PropTypes from "prop-types";
import {connect} from "react-redux";
import {IndexLinkContainer} from "react-router-bootstrap";
import {Glyphicon, DropdownButton, MenuItem, MenuItemLink} from "react-bootstrap";
import moment from 'moment';
import DeletePrompt from "../../components/common/DeletePrompt";

export class PingWidget extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      delete_show: false
    };

    this.showDeleteWidgetPrompt = this.showDeleteWidgetPrompt.bind(this);
    this.hideDeleteWidgetPrompt = this.hideDeleteWidgetPrompt.bind(this);
    this.deleteWidget= this.deleteWidget.bind(this);
    this.isOk = this.isOk.bind(this);
  }

  isOk(status) {
    return (status >= 200 && status < 300);
  }

  render() {
    const {widget} = this.props;
    let cssState = this.isOk(widget.statusCode) ? 'positive arrow-up' : 'negative arrow-down';
    let statusBoxCss = `ping-widget-status arrow ${cssState}`;
    return (
      <section className="widget">
        <header>
          <div className="widget-menu">
            <DropdownButton bsStyle="link" style={{right: "-15px",top: "0px", }} noCaret title={<Glyphicon glyph="wrench"/>}  id="1">
              <IndexLinkContainer to={"/ui/widget/edit/" + widget.dashboardId + "/" + widget.id}><MenuItem bsStyle="link">Edit</MenuItem></IndexLinkContainer>
              <MenuItem bsStyle="link" onSelect={() => this.showDeleteWidgetPrompt()}>Delete</MenuItem>
            </DropdownButton>
          </div>
          <p>{widget.name}</p>
        </header>

        <div className="body">
          <div className={statusBoxCss}> { this.isOk(widget.statusCode) ? 'Up' : 'Down'}</div>
          <div className="ping-widget-footer">
            <div className="footer-col">
              <div className="footer-label">Latency</div>
              <div className="footer-content">{widget.latency}ms</div>
            </div>
            <div className="footer-col">
              <div className="footer-label">Last</div>
              <div className="footer-content">{moment(widget.timestamp).format("HH:mm:ss")}</div>
            </div>
          </div>
        </div>

        <DeletePrompt show={this.state.delete_show} title={widget.name}
                      hideFn={this.hideDeleteWidgetPrompt} deleteFn={this.deleteWidget}/>
      </section>
    );
  }

  showDeleteWidgetPrompt() {
    this.setState({delete_show: true});
  }

  hideDeleteWidgetPrompt() {
    this.setState({delete_show: false});
  }

  deleteWidget() {
    this.props.dispatch({type: 'WIDGET_DELETE', widget: this.props.widget});
    this.hideDeleteWidgetPrompt();
  }

}

PingWidget.propTypes = {
  widget: PropTypes.object,
};

function mapStateToProps(state, own_params) {
  return {
  };
}

export default connect(mapStateToProps)(PingWidget);