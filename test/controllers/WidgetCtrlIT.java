package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import testhelpers.AbstractIT;
import common.exceptions.application.FormValidationException;
import common.exceptions.application.ModelNotFoundException;
import common.exceptions.application.ModelValidationException;
import models.Dashboard;
import models.Widget;
import models.widget.PingWidgetConf;
import models.widget.WidgetConf;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import views.WidgetForm;

import static common.seed.Seed.store;
import static common.seed.SeedDashboard.buildDashboard;
import static common.seed.SeedWidget.buildPingWidget;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;


public class WidgetCtrlIT extends AbstractIT {

    private String uri = "/api/widget";
    private Dashboard dashboardOne;
    private Widget widgetOne;
    private Widget widgetTwo;

    @Before
    public void setUp() {
        dashboardOne = store(buildDashboard("TjuEC3WcGVgMALvLtjzaCfgJQeXBaPfg", "dashboardOne"));
        widgetOne = store(buildPingWidget(dashboardOne, "widgetOne", "http://www.google.fi"));
        widgetTwo = store(buildPingWidget(dashboardOne, "widgetTwo", "http://www.hs.fi"));
    }

    @Test
    public void list() {
        Result result = doGet(uri.concat("/list/".concat(dashboardOne.getId().toString())));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.size()).isEqualTo(2);
    }

    @Test
    public void create() {
        PingWidgetConf conf = new PingWidgetConf("http://www.google.fi");
        WidgetForm form = new WidgetForm(dashboardOne.getId(), "newWidget", "PING", Json.toJson(conf));
        Result result = doPost(uri, Json.toJson(form));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.get("id").asLong()).isGreaterThan(0L);
        assertThat(resultBody.get("name").asText()).isEqualTo("newWidget");
    }

    @Test
    public void createWhenWidgetTypeUnknown() {
        PingWidgetConf conf = new PingWidgetConf("http://www.google.fi");
        WidgetForm form = new WidgetForm(dashboardOne.getId(), "newWidget", "UNKNOWN", Json.toJson(conf));

        Throwable t = catchThrowable(() -> doPost(uri, Json.toJson(form)));
        assertThat(t).isInstanceOf(FormValidationException.class);
        assertThat(((FormValidationException) t).validationErrors.get(0).message()).isEqualTo("widget.type.unknown");
    }

    @Test
    public void createWhenConfPartInvalid() {
        PingWidgetConf conf = new PingWidgetConf(null);
        WidgetForm form = new WidgetForm(dashboardOne.getId(), "newWidget", "PING", Json.toJson(conf));

        Throwable t = catchThrowable(() -> doPost(uri, Json.toJson(form)));
        assertThat(t).isInstanceOf(ModelValidationException.class);
        assertThat(t.getMessage()).isEqualTo("url must be defined");
    }

    @Test
    public void update() {
        WidgetConf conf = widgetOne.getWidgetConf();
        WidgetForm form = new WidgetForm(dashboardOne.getId(), "updatedName", "PING", Json.toJson(conf));
        Result result = doPut(uri.concat("/" + widgetOne.getId()), Json.toJson(form));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.get("name").asText()).isEqualTo("updatedName");
    }

    @Test
    public void updateWithMalformedData() {
        PingWidgetConf conf = new PingWidgetConf(null);
        WidgetForm form = new WidgetForm(dashboardOne.getId(), "updatedName", "PING", Json.toJson(conf));
        Throwable t = catchThrowable(() -> doPut(uri.concat("/" + widgetOne.getId()), Json.toJson(form)));

        assertThat(t).isInstanceOf(ModelValidationException.class);
        assertThat(t.getMessage()).isEqualTo("url must be defined");
    }

    @Test
    public void delete() {
        Result result = doDelete(uri.concat("/" + widgetOne.getId()));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
    }

    @Test
    public void deleteWithUnknownId() {
        Throwable t = catchThrowable(() -> doDelete(uri.concat("/" + 99999)));

        assertThat(t).isInstanceOf(ModelNotFoundException.class);
        assertThat(t.getMessage()).isEqualTo("not found: 99999");
    }

    @Test
    public void fetch() {
        Result result = doGet(uri.concat("/" + widgetOne.getId()));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.get("id").asLong()).isEqualTo(widgetOne.getId());
    }

    @Test
    public void fetchWithUnknownId() {
        Throwable t = catchThrowable(() -> doGet(uri.concat("/" + 99999)));

        assertThat(t).isInstanceOf(ModelNotFoundException.class);
        assertThat(t.getMessage()).isEqualTo("not found: 99999");
    }


}
