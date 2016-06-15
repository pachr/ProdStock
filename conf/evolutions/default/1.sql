# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table box (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  BOX_NUMBER                integer,
  box_type_id               varchar(255),
  command_id                varchar(255),
  INSTANCE_ID               integer(55),
  constraint pk_box primary key (ID))
;

create table box_type (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  HEIGHT                    integer,
  WIDTH                     integer,
  PRICE                     float,
  INSTANCE_ID               integer(55),
  constraint pk_box_type primary key (ID))
;

create table command (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  MIN_TIME                  integer,
  SENDING_TDATE             integer,
  REAL_TDATE                integer,
  FEE                       float,
  PRODUCT_ID_QUANTITY       integer,
  INSTANCE_ID               integer(55),
  constraint pk_command primary key (ID))
;

create table instance (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  constraint pk_instance primary key (ID))
;

create table pile (
  ID                        integer(55) auto_increment not null,
  WIDTH                     integer,
  HEIGHT_MAX                integer,
  BOX_ID                    integer(55),
  BOX_COMMAND_ID            integer(55),
  PRODUCT_TYPE_ID           integer(55),
  constraint uq_pile_BOX_ID unique (BOX_ID),
  constraint uq_pile_BOX_COMMAND_ID unique (BOX_COMMAND_ID),
  constraint uq_pile_PRODUCT_TYPE_ID unique (PRODUCT_TYPE_ID),
  constraint pk_pile primary key (ID))
;

create table product (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  PRODUCT_TYPE_ID           integer(55),
  START_PRODUCTION          varchar(255),
  COMMAND_ID                integer(55),
  PRODUCT_LINE_ID           integer(55),
  BOX_ID                    integer(55),
  INSTANCE_ID               integer(55),
  constraint pk_product primary key (ID))
;

create table product_line (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  PRODUCT_LINE_NUMBER       integer,
  INSTANCE_ID               integer(55),
  constraint pk_product_line primary key (ID))
;

create table product_line_type (
  ID                        integer(55) auto_increment not null,
  PRODUCT_LINE_NUMBER       integer,
  INSTANCE_ID               integer(55),
  constraint pk_product_line_type primary key (ID))
;

create table product_type (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  SET_UP_TIME               integer,
  PRODUCTION_TIME           integer,
  HEIGHT                    integer,
  WIDTH                     integer,
  MAX_UNIT                  integer,
  INSTANCE_ID               integer(55),
  constraint pk_product_type primary key (ID))
;

create table solution (
  ID                        integer(55) auto_increment not null,
  NAME                      varchar(255),
  FEE                       float,
  SENDING_DATE              integer,
  EVAL_SCORE                float,
  INSTANCE_ID               integer(55),
  constraint pk_solution primary key (ID))
;

alter table box add constraint fk_box_instanceId_1 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_box_instanceId_1 on box (INSTANCE_ID);
alter table box_type add constraint fk_box_type_instanceId_2 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_box_type_instanceId_2 on box_type (INSTANCE_ID);
alter table command add constraint fk_command_instanceId_3 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_command_instanceId_3 on command (INSTANCE_ID);
alter table pile add constraint fk_pile_boxId_4 foreign key (BOX_ID) references box (ID) on delete restrict on update restrict;
create index ix_pile_boxId_4 on pile (BOX_ID);
alter table pile add constraint fk_pile_commandPileId_5 foreign key (BOX_COMMAND_ID) references command (ID) on delete restrict on update restrict;
create index ix_pile_commandPileId_5 on pile (BOX_COMMAND_ID);
alter table pile add constraint fk_pile_productTypeId_6 foreign key (PRODUCT_TYPE_ID) references product_type (ID) on delete restrict on update restrict;
create index ix_pile_productTypeId_6 on pile (PRODUCT_TYPE_ID);
alter table product add constraint fk_product_productTypeId_7 foreign key (PRODUCT_TYPE_ID) references product_type (ID) on delete restrict on update restrict;
create index ix_product_productTypeId_7 on product (PRODUCT_TYPE_ID);
alter table product add constraint fk_product_commandId_8 foreign key (COMMAND_ID) references command (ID) on delete restrict on update restrict;
create index ix_product_commandId_8 on product (COMMAND_ID);
alter table product add constraint fk_product_productLineId_9 foreign key (PRODUCT_LINE_ID) references product_line (ID) on delete restrict on update restrict;
create index ix_product_productLineId_9 on product (PRODUCT_LINE_ID);
alter table product add constraint fk_product_boxId_10 foreign key (BOX_ID) references box (ID) on delete restrict on update restrict;
create index ix_product_boxId_10 on product (BOX_ID);
alter table product add constraint fk_product_instanceId_11 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_product_instanceId_11 on product (INSTANCE_ID);
alter table product_line add constraint fk_product_line_instanceId_12 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_product_line_instanceId_12 on product_line (INSTANCE_ID);
alter table product_line_type add constraint fk_product_line_type_instanceId_13 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_product_line_type_instanceId_13 on product_line_type (INSTANCE_ID);
alter table product_type add constraint fk_product_type_instanceId_14 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_product_type_instanceId_14 on product_type (INSTANCE_ID);
alter table solution add constraint fk_solution_instanceId_15 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_solution_instanceId_15 on solution (INSTANCE_ID);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table box;

drop table box_type;

drop table command;

drop table instance;

drop table pile;

drop table product;

drop table product_line;

drop table product_line_type;

drop table product_type;

drop table solution;

SET FOREIGN_KEY_CHECKS=1;

