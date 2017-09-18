import { takeLatest, channel, eventChannel } from "redux-saga";
import { fork } from "redux-saga/effects";
import { dashboardList, dashboardAdd, dashboardUpdate, dashboardDelete } from "./dashboards";
import { widgetAdd, widgetUpdate, widgetDelete } from "./widgets";
import { flow } from "./sockets";

export function* sagas() {
  yield [
    fork(takeLatest, 'DASHBOARD_FETCH_LIST', dashboardList),
    fork(takeLatest, 'DASHBOARD_CREATE', dashboardAdd),
    fork(takeLatest, 'DASHBOARD_UPDATE', dashboardUpdate),
    fork(takeLatest, 'DASHBOARD_DELETE', dashboardDelete),
    fork(takeLatest, 'WIDGET_CREATE', widgetAdd),
    fork(takeLatest, 'WIDGET_UPDATE', widgetUpdate),
    fork(takeLatest, 'WIDGET_DELETE', widgetDelete),
    fork(flow),
  ];
}