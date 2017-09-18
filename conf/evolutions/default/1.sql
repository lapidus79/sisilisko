# --- !Ups

create table dashboard (
  id                            bigint not null,
  token_id                      varchar(64) not null,
  name                          varchar(255),
  created                       timestamp not null,
  updated                       timestamp not null,
  version                       bigint not null,
  constraint pk_dashboard primary key (id),
  constraint unique_dashboard_token_id unique (token_id)
);
create sequence dashboard_seq;
create index index_dashboard_token_id on dashboard(token_id);

create table widget (
  id                            bigint not null,
  dashboard_id                  bigint not null,
  name                          varchar(255),
  type                          varchar(12),
  widget_conf                   jsonb not null,
  created                       timestamp not null,
  updated                       timestamp not null,
  version                       bigint not null,
  constraint pk_widget primary key (id)
);
create sequence widget_seq;
create index index_widget_dashboard_id on widget (dashboard_id);

alter table widget add constraint fk_widget_dasboard_id foreign key (dashboard_id) references dashboard (id) on delete CASCADE on update restrict;

# --- !Downs

alter table widget drop constraint if exists fk_widget_dasboard_id;
drop index if exists index_widget_dashboard_id;

drop table if exists widget cascade;
drop sequence if exists widget_seq;

alter table dashboard drop constraint if exists index_dashboard_token_id;
drop index if exists index_dashboard_token_id;

drop table if exists dashboard cascade;
drop sequence if exists dashboard_seq;
