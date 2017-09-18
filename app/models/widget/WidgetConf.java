package models.widget;

import com.fasterxml.jackson.databind.JsonNode;
import common.exceptions.application.ModelValidationException;
import models.Widget;
import play.libs.Json;

public abstract class WidgetConf {

    public WidgetConf() {}

    public abstract void validate() throws ModelValidationException;

    public static WidgetConf build(JsonNode payloadJson, Widget.Type type) {

        Class clazz;
        if (type.equals(Widget.Type.PING)) {
            clazz = PingWidgetConf.class;
        } else {
            throw new RuntimeException("WidgetConf.build(): type not supported " + type);
        }
        return (WidgetConf) Json.fromJson(payloadJson, clazz);
    }

}
