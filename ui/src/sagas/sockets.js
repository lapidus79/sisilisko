import { eventChannel } from 'redux-saga';
import { fork, take, call, put, cancel } from 'redux-saga/effects';

function connect() {
  const socket = new WebSocket(`ws://localhost:9000/ws`);
  return new Promise(resolve => {
    socket.onopen = () => {
      resolve(socket);
    };
  });
}

function subscribe(socket) {
  return eventChannel(emit => {
    socket.onmessage = ((event) => {
      try {
        const data = JSON.parse(event.data);
        emit(data);
      } catch (e) {
        console.warn("failed to parse ws message", event);
      }
    });

    return () => {};
  });
}

function* readMessages(socket) {
  const channel = yield call(subscribe, socket);
  while (true) {
    let eventJson = yield take(channel);
    if (eventJson.command === "dashboard_update") {
      yield put({type: "STREAM_DASHBOARD_UPDATE", dashboard: eventJson.payload});
    } else if (eventJson.command === "widget_update") {
      yield put({type: "STREAM_WIDGET_UPDATE", widget: eventJson.payload});
    } else {
      console.warn("unknown message received from backend", eventJson);
    }
  }
}

function* writeMessages(socket) {
  while (true) {
    const {command, payload} = yield take("WS_SEND_MESSAGE");

    if (command === "SUBSCRIBE") {
      socket.send(JSON.stringify({command: command, value: payload}));
    } else if (command === "UNSUBSCRIBE") {
      socket.send(JSON.stringify({command: command, value: payload}));
    } else {
      console.warn("WS-SEND: unknown send command ", command);
    }
  }
}

function* handleIO(socket) {
  yield fork(readMessages, socket);
  yield fork(writeMessages, socket);
}

export function* flow() {
  while (true) {
    yield take('WS_OPEN_CONNECTION');
    const socket = yield call(connect);

    let poller = 0;
    function keepAlive() {
      if (socket.readyState == socket.OPEN) {
        socket.send(JSON.stringify({ command: "keepalive"}));
      }
      poller = setTimeout(keepAlive, 20000);
    }
    keepAlive();

    const task = yield fork(handleIO, socket);

    let action = yield take('WS_CLOSE_CONNECTION');
    yield cancel(task);
    if (timerId) {
      clearTimeout(timerId);
    }
    socket.close();
  }
}

