
function errorHandler(response) {
  if (!response.ok) {
    if (response.status >= 400 && response.status < 500) {
      response.body.text().then((t) => console.error("request failed with: code=["+response.status+"], body=["+t+"]"));
      throw Error(response.status);
    } else {
      console.error("request failed with: status=["+response.status+"]");
      throw Error(response.status);
    }
  }
  return response;
}

function headersConf() {
  return { "Content-Type": 'application/json' };
}

export { errorHandler, headersConf };