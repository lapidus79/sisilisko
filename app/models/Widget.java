package models;


import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Finder;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import common.exceptions.application.ModelValidationException;
import models.widget.WidgetConf;
import play.libs.Json;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="widget")
public class Widget extends AbstractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Dashboard dashboard;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private String widgetConf;

    @CreatedTimestamp
    private Date created = new Date();

    @UpdatedTimestamp
    private Date updated = new Date();

    @Version
    private Long version;

    public static Finder<Long, Widget> find = new Finder<>(Widget.class);

    public void validate() throws ModelValidationException {}

    public enum Type {
        PING
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WidgetConf getWidgetConf() {
        JsonNode json = Json.parse(widgetConf);
        return WidgetConf.build(json, getType());
    }

    public void setWidgetConf(WidgetConf widgetConf) {
        this.widgetConf = Json.stringify(Json.toJson(widgetConf));
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

