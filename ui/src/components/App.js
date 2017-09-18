import React from "react";
import {connect} from "react-redux";
import TopNavbar from '../components/common/TopNavbar';
import "../stylesheets/main.scss";

export class App extends React.Component {

  componentWillMount() {
    this.props.dispatch({type: 'DASHBOARD_FETCH_LIST'});
    this.props.dispatch({type: 'WS_OPEN_CONNECTION'});
  }

  render() {
    const {children, navbar} = this.props;
    return (
      <div>
        <TopNavbar title={navbar.title}/>
        <div className="container" style={{marginTop: '60px'}}>
          {children}
        </div>
      </div>
    );
  }

}

function mapStateToProps(state) {
  return {
    navbar: state.navbar || [],
  };
}

export default connect(mapStateToProps)(App);


