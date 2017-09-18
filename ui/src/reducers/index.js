import { combineReducers } from "redux";
import { routerReducer } from "react-router-redux";
import { reducer as formReducer } from "redux-form";
import dashboards from "./dashboards";
import navbar from "./navbar";
import streamingDashboards from "./streamingDashboards";
import streamingWidgets from "./streamingWidgets";

export const reducers = combineReducers({
  routing: routerReducer,
  form: formReducer.plugin({
    "dashboard_edit": (state, action) => {
      switch(action.type) {
        case "@@router/LOCATION_CHANGE":
          return undefined;
        default:
          return state;
      }
    },
    "widget_edit": (state, action) => {
      switch(action.type) {
        case "@@router/LOCATION_CHANGE":
          return undefined;
        default:
          return state;
      }
    },
  }),
  dashboards: dashboards,
  streamingDashboards: streamingDashboards,
  streamingWidgets: streamingWidgets,
  navbar: navbar
});
