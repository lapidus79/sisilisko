package controllers;


import akka.actor.ActorSystem;
import com.google.inject.Inject;
import common.misc.Helpers;
import common.services.akka.DashboardAkkaFacade;
import models.Dashboard;
import models.repositories.DashboardRepository;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Result;
import views.DashboardForm;
import views.DashboardJson;
import views.DashboardListJson;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DashboardCtrl extends ApplicationCtrl {

    private final Form<DashboardForm> boardForm;
    private final DashboardAkkaFacade boardAkkaFacade;


    @Inject
    public DashboardCtrl(final HttpExecutionContext ec,
                         final DashboardRepository boardRepo,
                         final FormFactory formFactory,
                         final DashboardAkkaFacade boardAkkaFacade,
                         final ActorSystem as) {
        super(boardRepo, as, ec);
        this.boardAkkaFacade = boardAkkaFacade;
        this.boardForm = formFactory.form(DashboardForm.class);
    }

    public CompletionStage<Result> list() {
        return CompletableFuture
                .supplyAsync(boardRepo::findAll, dbExecCtx)
                .thenApplyAsync(DashboardListJson::new, ec.current())
                .thenApply(this::toOkJson);
    }

    public CompletionStage<Result> fetch(final Long id) {
        return fetchDashboard(id)
                .thenApplyAsync(DashboardJson::new, ec.current())
                .thenApply(this::toOkJson);
    }

    public CompletionStage<Result> delete(final Long id) {
        return fetchDashboard(id)
                .thenApplyAsync(boardRepo::delete, dbExecCtx)
                .thenApplyAsync(boardAkkaFacade::terminateDashboard, ec.current())
                .thenApply(DashboardJson::new)
                .thenApply(this::toOkJson);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create() {
        return CompletableFuture
                .supplyAsync(boardForm::bindFromRequest, ec.current())
                .thenApply(this::validateForm)
                .thenApply(this::newDashboard)
                .thenApplyAsync(boardRepo::save, dbExecCtx)
                .thenApplyAsync(boardAkkaFacade::addDashboard, ec.current())
                .thenApply(DashboardJson::new)
                .thenApply(this::toOkJson);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> update(final Long id) {
        return CompletableFuture
                .supplyAsync(boardForm::bindFromRequest, ec.current())
                .thenApply(this::validateForm)
                .thenCombine(
                        fetchDashboard(id),
                        (f,d) -> populateDashboard(d,f.get())
                )
                .thenApplyAsync(boardRepo::save, dbExecCtx)
                .thenApplyAsync(boardAkkaFacade::refreshDashboard, ec.current())
                .thenApply(DashboardJson::new)
                .thenApply(this::toOkJson);
    }

    private Dashboard populateDashboard(final Dashboard dashboard,
                                        final DashboardForm form) {
        dashboard.setName(form.getName());
        if (dashboard.getId() == null) {
            dashboard.setTokenId(Helpers.generateToken());
        }
        return dashboard;
    }

    private Dashboard newDashboard(final Form<DashboardForm> form) {
        return populateDashboard(new Dashboard(), form.get());
    }

}
