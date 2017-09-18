package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import testhelpers.AbstractIT;
import common.exceptions.application.FormValidationException;
import common.exceptions.application.ModelNotFoundException;
import models.Dashboard;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import views.DashboardForm;

import static common.seed.Seed.store;
import static common.seed.SeedDashboard.buildDashboard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class DashboardCtrlIT extends AbstractIT {

    private String uri = "/api/dashboard";
    private Dashboard dashboardOne;
    private Dashboard dashboardTwo;

    @Before
    public void setUp() {
        dashboardOne = store(buildDashboard("TjuEC3WcGVgMALvLtjzaCfgJQeXBaPfg", "dashboardOne"));
        dashboardTwo = store(buildDashboard("Vsk3NdozJV395hNI0ssrsI3Asun236We", "dashboardTwo"));
    }

    @Test
    public void list() {
        Result result = doGet(uri.concat("/list"));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.size()).isEqualTo(2);
    }

    @Test
    public void create() {
        DashboardForm form = new DashboardForm("newDashboard");
        Result result = doPost(uri, Json.toJson(form));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.get("id").asLong()).isGreaterThan(0L);
        assertThat(resultBody.get("name").asText()).isEqualTo("newDashboard");
    }

    @Test
    public void createWithMalformedData() {
        DashboardForm form = new DashboardForm(null);
        Throwable t = catchThrowable(() -> doPost(uri, Json.toJson(form)));

        assertThat(t).isInstanceOf(FormValidationException.class);
        assertThat(((FormValidationException) t).validationErrors.get(0).message()).isEqualTo("dashboard.name.required");
    }

    @Test
    public void update() {
        DashboardForm form = new DashboardForm("updatedName");
        Result result = doPut(uri.concat("/" + dashboardOne.getId()), Json.toJson(form));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.get("name").asText()).isEqualTo("updatedName");
    }

    @Test
    public void updateWithMalformedData() {
        DashboardForm form = new DashboardForm(null);
        Throwable t = catchThrowable(() -> doPut(uri.concat("/" + dashboardOne.getId()), Json.toJson(form)));

        assertThat(t).isInstanceOf(FormValidationException.class);
        assertThat(((FormValidationException) t).validationErrors.get(0).message()).isEqualTo("dashboard.name.required");
    }

    @Test
    public void delete() {
        Result result = doDelete(uri.concat("/" + dashboardOne.getId()));

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
        Result result = doGet(uri.concat("/" + dashboardOne.getId()));

        assertThat(result.status()).isEqualTo(Http.Status.OK);
        JsonNode resultBody = Json.parse(Helpers.contentAsString(result));
        assertThat(resultBody.get("id").asLong()).isEqualTo(dashboardOne.getId());
    }

    @Test
    public void fetchWithUnknownId() {
        Throwable t = catchThrowable(() -> doGet(uri.concat("/" + 99999)));

        assertThat(t).isInstanceOf(ModelNotFoundException.class);
        assertThat(t.getMessage()).isEqualTo("not found: 99999");
    }

}