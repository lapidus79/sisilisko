package testhelpers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static play.test.Helpers.*;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;

public abstract class AbstractIT extends WithApplication {

    /**
     * Setup fakeApplication to run integration tests
     */
    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    protected Result doGet(String uri) {
        return doRequest(uri,null, GET);
    }

    protected Result doPost(String uri, JsonNode payload) {
        return doRequest(uri, payload, POST);
    }

    protected Result doPut(String uri, JsonNode payload) {
        return doRequest(uri, payload, PUT);
    }

    protected Result doDelete(String uri) {
        return doRequest(uri, null, DELETE);
    }

    private Result doRequest(String uri, JsonNode payload, String method) {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(method)
                .uri(uri);
        if (payload != null) {
            request.bodyJson(payload);
        }
        return route(app, request);
    }
}

