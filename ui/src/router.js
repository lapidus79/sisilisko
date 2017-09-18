import React from "react";
import { Router, Route, IndexRoute } from "react-router";
import { history } from "./store.js";
import App from "./components/App";
import IndexView from "./components/IndexView";
import DashboardView from "./components/DashboardView";
import DashboardEditView from "./components/DashboardEditView";
import WidgetEditView from "./components/WidgetEditView";
import NotFoundView from "./components/NotFoundView";

const router = (
  <Router onUpdate={() => window.scrollTo(0, 0)} history={history}>
    <Route path="/" component={App}>
      <IndexRoute component={IndexView}/>
      <Route path="/d(/:dashboardTokenId)" component={DashboardView}/>
      <Route path="/ui/dashboard/edit(/:id)" component={DashboardEditView}/>
      <Route path="/ui/widget/edit/:dashboardId(/:id)" component={WidgetEditView}/>
      <Route path="*" component={NotFoundView}/>
    </Route>
  </Router>
);

export { router };
