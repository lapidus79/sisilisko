package controllers;


import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import common.exceptions.application.ModelNotFoundException;
import common.services.akka.WidgetAkkaFacade;
import models.Dashboard;
import models.Widget;
import models.repositories.DashboardRepository;
import models.repositories.WidgetRepository;
import models.widget.WidgetConf;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Result;
import views.WidgetForm;
import views.WidgetJson;
import views.WidgetListJson;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WidgetCtrl extends ApplicationCtrl {

    private final WidgetRepository widRepo;
    private final WidgetAkkaFacade widAkkaFacade;
    private final Form<WidgetForm> widForm;

    @Inject
    public WidgetCtrl(final HttpExecutionContext ec,
                      final WidgetRepository widRepo,
                      final DashboardRepository boardRepo,
                      final FormFactory formFactory,
                      final WidgetAkkaFacade widAkkaFacade,
                      final ActorSystem as) {
        super(boardRepo, as, ec);
        this.widRepo = widRepo;
        this.widAkkaFacade = widAkkaFacade;
        this.widForm = formFactory.form(WidgetForm.class);
    }

    public CompletionStage<Result> list(final Long dashboardId) {
        return CompletableFuture
                .completedFuture(dashboardId)
                .thenApplyAsync(widRepo::findAll, dbExecCtx)
                .thenApplyAsync(WidgetListJson::new, ec.current())
                .thenApply(this::toOkJson);
    }

    public CompletionStage<Result> fetch(final Long id) {
        return fetchWidget(id)
                .thenApplyAsync(WidgetJson::new, ec.current())
                .thenApply(this::toOkJson);
    }

    public CompletionStage<Result> delete(final Long id) {
        return fetchWidget(id)
                .thenApplyAsync(widRepo::delete, dbExecCtx)
                .thenApplyAsync(widAkkaFacade::terminateWidget, ec.current())
                .thenApply(WidgetJson::new)
                .thenApply(this::toOkJson);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create() {
        return CompletableFuture
                .supplyAsync(widForm::bindFromRequest, ec.current())
                .thenApply(this::validateForm)
                .thenCompose(
                        (f) -> fetchDashboard(f.get().getDashboardId())
                                .thenApplyAsync((d) ->
                                        populateWidgetMeta(
                                                new Widget(),
                                                f.get(),
                                                request().body().asJson().get("conf"),
                                                d),
                                        ec.current())
                )
                .thenApplyAsync(widRepo::save, dbExecCtx)
                .thenApplyAsync(widAkkaFacade::addWidget, ec.current())
                .thenApply(WidgetJson::new)
                .thenApply(this::toOkJson);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> update(final Long id) {
        return CompletableFuture
                .supplyAsync(widForm::bindFromRequest, ec.current())
                .thenApply(this::validateForm)
                .thenCombine(
                        fetchWidget(id),
                        (f, w) -> populateWidgetMeta(w,
                                f.get(),
                                request().body().asJson().get("conf"),
                                w.getDashboard())
                )
                .thenApplyAsync(widRepo::save, dbExecCtx)
                .thenApply(widAkkaFacade::refreshWidget)
                .thenApply(WidgetJson::new)
                .thenApply(this::toOkJson);
    }

    private CompletionStage<Widget> fetchWidget(final Long id) {
        return CompletableFuture.supplyAsync(() ->
                widRepo.findById(id)
                        .orElseThrow(() -> new ModelNotFoundException(id)), dbExecCtx);
    }

    private Widget populateWidgetMeta(final Widget widget,
                                      final WidgetForm form,
                                      final JsonNode widgetConf,
                                      final Dashboard d) {
        widget.setName(form.getName());
        if (widget.getId() == null) {
            widget.setType(Widget.Type.valueOf(form.getType()));
        }
        WidgetConf conf = WidgetConf.build(widgetConf, widget.getType());
        conf.validate();
        widget.setWidgetConf(conf);
        widget.setDashboard(d);
        return widget;
    }

}
