import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router";
import { Navbar, Nav, NavItem } from "react-bootstrap";

export default class TopNavbar extends React.Component {

  render() {
    const {title} = this.props;
    return (
      <Navbar bsStyle="inverse" fluid fixedTop>
        <Navbar.Header>
          <Navbar.Brand>
            <Link to="/">
              <img className="navbar-logo" src="/media/sisilisko_logo.svg"/>
            </Link>
          </Navbar.Brand>
        </Navbar.Header>
        <Nav className='navbar-text navbar-center'>{title}</Nav>
      </Navbar>
    );
  }

}

TopNavbar.propTypes = {
  title: PropTypes.string
};

