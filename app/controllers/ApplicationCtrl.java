package controllers;

import actors.ActorRoutes;
import akka.actor.ActorSystem;
import common.exceptions.application.FormValidationException;
import common.exceptions.application.ModelNotFoundException;
import models.Dashboard;
import models.repositories.DashboardRepository;
import play.data.Form;
import play.libs.Json;
import play.libs.concurrent.HttpExecution;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

public class ApplicationCtrl extends Controller {

    protected final DashboardRepository boardRepo;
    protected final Executor dbExecCtx;
    protected final HttpExecutionContext ec;

    public ApplicationCtrl(final DashboardRepository boardRepo,
                           final ActorSystem as,
                           final HttpExecutionContext ec) {
        this.boardRepo = boardRepo;
        this.dbExecCtx = HttpExecution.fromThread((Executor) as.dispatchers().lookup(ActorRoutes.DB_EXEC_CTX));
        this.ec = ec;
    }

    protected Result toOkJson(final Object o) {
        return ok(Json.toJson(o));
    }


    protected  <U> Form<U> validateForm(final Form<U> form) {
        if (form.hasErrors()) {
            throw new FormValidationException(form.allErrors());
        }
        return form;
    }

    protected CompletionStage<Dashboard> fetchDashboard(final Long id) {
        return CompletableFuture.supplyAsync(() ->
                boardRepo.findById(id)
                        .orElseThrow(() -> new ModelNotFoundException(id)), dbExecCtx);
    }

}
