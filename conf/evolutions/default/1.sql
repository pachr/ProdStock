# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table box (
  ID                        varchar(55) not null,
  box_type_id               varchar(255),
  command_id                varchar(255),
  INSTANCE_ID               varchar(55),
  constraint pk_box primary key (ID))
;

create table box_type (
  ID                        varchar(55) not null,
  HEIGHT                    integer,
  WIDTH                     integer,
  PRICE                     float,
  INSTANCE_ID               varchar(55),
  constraint pk_box_type primary key (ID))
;

create table commande (
  ID                        varchar(55) not null,
  MIN_TIME                  integer,
  SENDING_TDATE             integer,
  FEE                       float,
  PRODUCT_ID_QUANTITY       integer,
  INSTANCE_ID               varchar(55),
  constraint pk_commande primary key (ID))
;

create table instance (
  ID                        varchar(55) not null,
  constraint pk_instance primary key (ID))
;

create table product_line (
  ID                        varchar(55) not null,
  PRODUCT_LINE_NUMBER       integer,
  INSTANCE_ID               varchar(55),
  constraint pk_product_line primary key (ID))
;

create table product_line_type (
  ID                        varchar(55) not null,
  PRODUCT_LINE_NUMBER       integer,
  INSTANCE_ID               varchar(55),
  constraint pk_product_line_type primary key (ID))
;

create table product_type (
  ID                        varchar(55) not null,
  SET_UP_TIME               integer,
  PRODUCTION_TIME           integer,
  HEIGHT                    integer,
  WIDTH                     integer,
  MAX_UNIT                  integer,
  INSTANCE_ID               varchar(55),
  constraint pk_product_type primary key (ID))
;

create table produit (
  ID                        varchar(55) not null,
  PRODUCT_TYPE_ID           varchar(55),
  START_PRODUCTION          varchar(255),
  COMMAND_ID                varchar(55),
  PRODUCT_LINE_ID           varchar(55),
  BOX_ID                    varchar(55),
  INSTANCE_ID               varchar(55),
  constraint pk_produit primary key (ID))
;

create table solution (
  ID                        varchar(55) not null,
  FEE                       float,
  SENDING_DATE              integer,
  EVAL_SCORE                float,
  INSTANCE_ID               varchar(55),
  constraint pk_solution primary key (ID))
;

alter table box add constraint fk_box_instanceId_1 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_box_instanceId_1 on box (INSTANCE_ID);
alter table box_type add constraint fk_box_type_instanceId_2 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_box_type_instanceId_2 on box_type (INSTANCE_ID);
alter table commande add constraint fk_commande_instanceId_3 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_commande_instanceId_3 on commande (INSTANCE_ID);
alter table product_line add constraint fk_product_line_instanceId_4 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_product_line_instanceId_4 on product_line (INSTANCE_ID);
alter table product_line_type add constraint fk_product_line_type_instanceId_5 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_product_line_type_instanceId_5 on product_line_type (INSTANCE_ID);
alter table product_type add constraint fk_product_type_instanceId_6 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_product_type_instanceId_6 on product_type (INSTANCE_ID);
alter table produit add constraint fk_produit_productTypeId_7 foreign key (PRODUCT_TYPE_ID) references product_type (ID) on delete restrict on update restrict;
create index ix_produit_productTypeId_7 on produit (PRODUCT_TYPE_ID);
alter table produit add constraint fk_produit_commandId_8 foreign key (COMMAND_ID) references commande (ID) on delete restrict on update restrict;
create index ix_produit_commandId_8 on produit (COMMAND_ID);
alter table produit add constraint fk_produit_productLineId_9 foreign key (PRODUCT_LINE_ID) references product_line (ID) on delete restrict on update restrict;
create index ix_produit_productLineId_9 on produit (PRODUCT_LINE_ID);
alter table produit add constraint fk_produit_boxId_10 foreign key (BOX_ID) references box (ID) on delete restrict on update restrict;
create index ix_produit_boxId_10 on produit (BOX_ID);
alter table produit add constraint fk_produit_instanceId_11 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_produit_instanceId_11 on produit (INSTANCE_ID);
alter table solution add constraint fk_solution_instanceId_12 foreign key (INSTANCE_ID) references instance (ID) on delete restrict on update restrict;
create index ix_solution_instanceId_12 on solution (INSTANCE_ID);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table box;

drop table box_type;

drop table commande;

drop table instance;

drop table product_line;

drop table product_line_type;

drop table product_type;

drop table produit;

drop table solution;

SET FOREIGN_KEY_CHECKS=1;

