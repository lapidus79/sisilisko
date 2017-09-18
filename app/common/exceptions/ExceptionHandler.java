package common.exceptions;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import common.exceptions.application.ApplicationException;
import common.exceptions.application.FormValidationException;
import common.exceptions.application.ModelNotFoundException;
import common.exceptions.application.ModelValidationException;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ExceptionHandler extends DefaultHttpErrorHandler {


    @Inject
    public ExceptionHandler(final Config config,
                            final Environment environment,
                            final OptionalSourceMapper sourceMapper,
                            final Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    public CompletionStage<Result> onServerError(final Http.RequestHeader request,
                                                 final Throwable exception) {

        if (exception instanceof ApplicationException) {
            return handleApplicationError(exception);
        } else {
            // All other runtime exceptions thrown -> lets return internal server error
            Logger.error("ErrorHandler: " + exception.getMessage());
            exception.printStackTrace();
            return CompletableFuture.completedFuture(Results.internalServerError("An unknown error occurred "));
        }
    }

    /**
     * Translate each application level error to correct http response code and return a json response as well
     */
    CompletionStage<Result> handleApplicationError(final Throwable e) {

        if (e instanceof FormValidationException) {
            return toResult(Http.Status.UNPROCESSABLE_ENTITY, e.getMessage());
        } else if (e instanceof ModelNotFoundException) {
            return toResult(Http.Status.NOT_FOUND, e.getMessage());
        } else if (e instanceof ModelValidationException) {
            return toResult(Http.Status.UNPROCESSABLE_ENTITY, e.getMessage());
        } else {
            return toResult(Http.Status.BAD_REQUEST, e.getMessage());
        }
    }

    CompletionStage<Result> toResult(final int statusCode,
                                     final String errorMsg) {
        Result r = Results.status(statusCode, Json.toJson(errorMsg));
        return CompletableFuture.completedFuture(r);
    }

}