import {call, put} from "redux-saga/effects";
import DashboardApi from "../api/DashboardApi";

export function* dashboardList(action) {
  try {
    const dashboards = yield call(DashboardApi.list);
    yield put({type: 'DASHBOARD_LIST_SAVE', dashboards: dashboards});
  } catch (e) {
    console.error("dashboardList(): fetching dashboards from backend failed ", e);
  }
}

export function* dashboardAdd(action) {
  try {
    const dashboard = yield call(DashboardApi.create, action.dashboard);
    yield put({type: 'DASHBOARD_CREATE_SAVE', dashboard: dashboard});
    action.callbackSuccess();
  } catch (e) {
    console.error("dashboardAdd(): saving new dashboard to backend failed ", e);
  }
}

export function* dashboardUpdate(action) {
  try {
    const dashboard = yield call(DashboardApi.update, action.dashboard);
    yield put({type: 'DASHBOARD_UPDATE_SAVE', dashboard: dashboard});
    action.callbackSuccess();
  } catch (e) {
    console.error("dashboardEdit(): updating dashboard to backend failed ", e);
  }
}

export function* dashboardDelete(action) {
  try {
    yield call(DashboardApi.delete, action.dashboard);
    yield put({type: 'DASHBOARD_DELETE_SAVE', dashboard: action.dashboard});
  } catch (e) {
    console.error("dashboardDelete(): deleting of dashboard failed ", e);
  }
}

