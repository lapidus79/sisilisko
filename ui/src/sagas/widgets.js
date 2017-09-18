import {call, put} from "redux-saga/effects";
import WidgetApi from "../api/WidgetApi";

export function* widgetAdd(action) {
  try {
    yield call(WidgetApi.create, action.widget);
    action.callbackSuccess();
  } catch (e) {
    console.error("widgetAdd(): add failed ", e);
  }
}

export function* widgetUpdate(action) {
  try {
    yield call(WidgetApi.update, action.widget);
    action.callbackSuccess();
  } catch (e) {
    console.error("widgetUpdate(): update failed ", e);
  }
}

export function* widgetDelete(action) {
  try {
    yield call(WidgetApi.delete, action.widget);
  } catch (e) {
    console.error("widgetDelete(): deleting failed ", e);
  }
}

