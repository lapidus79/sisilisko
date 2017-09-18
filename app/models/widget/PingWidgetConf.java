package models.widget;

import common.exceptions.application.ModelValidationException;
import org.apache.commons.lang3.Validate;

public class PingWidgetConf extends WidgetConf {

    private String url;
    private Integer interval = 5;

    public PingWidgetConf() {}

    public PingWidgetConf(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public void validate() throws ModelValidationException {

        try {
            Validate.notBlank(getUrl(), "url must be defined");
            Validate.notNull(getInterval(), "interval must be defined");
            Validate.inclusiveBetween(3, 86400, getInterval(), "interval must be between 3 and 86400");
            Validate.matchesPattern(getUrl(), "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "url must be valid");
        } catch (Exception e) {
            throw new ModelValidationException(e.getMessage());
        }
    }

}
