create table if not exists ban_record
(
    id            serial primary key,
    qq_id         bigint    not null,
    from_group    bigint    not null,
    status        int       not null,
    last_ban_date timestamp not null,
    ban_time      int       not null
);

create table if not exists dream
(
    id      bigserial primary key,
    element varchar(2048) not null,
    from_qq bigint        not null,
    date    timestamp     not null,
    qq_name varchar(64)   not null
);

create table if not exists ffxiv_group
(
    id         serial primary key,
    name       varchar(25) not null,
    from_group bigint      not null
);

create table if not exists ffxiv_house
(
    id               serial primary key,
    name             varchar(25) not null,
    last_update_time timestamp   not null
);

create table if not exists ffxiv_hunt
(
    id             serial primary key,
    item_rank      int          not null,
    item_name      varchar(255) not null,
    item_count     varchar(255) not null,
    value_required varchar(255) not null,
    gain_exp       varchar(255) not null
);

create table if not exists ffxiv_item
(
    id        serial primary key,
    item_name varchar(255) not null,
    item_type int          not null
);

create table if not exists ffxiv_plant
(
    id             serial primary key,
    item_rank      int          not null,
    item_name      varchar(255) not null,
    item_count     varchar(255) not null,
    value_required varchar(255) not null,
    gain_exp       varchar(255) not null
);

create table if not exists ffxiv_stone
(
    id             serial primary key,
    item_rank      int          not null,
    item_name      varchar(255) not null,
    item_count     varchar(255) not null,
    value_required varchar(255) not null,
    gain_exp       varchar(255) not null
);

create table if not exists history_today
(
    id         serial primary key,
    history    text         not null,
    year       varchar(255) not null,
    event_date varchar(32)  not null,
    event_type int          not null,
    from_group bigint
);

create table if not exists kill_record
(
    id           serial primary key,
    qq_id        bigint        not null,
    from_group   bigint        not null,
    killed_times int default 0,
    kill_times   int default 0
);

create table if not exists sub_task
(
    id           bigserial primary key,
    reciver_type int          not null,
    reciver_id   bigint       not null,
    args         text         not null,
    sub_name     varchar(255) not null
);

create table if not exists openai_chat_message
(
    id          bigserial primary key,
    gmt_create  timestamp   not null,
    context_id  varchar(64) not null,
    message_key int         not null,
    content     text,
    role        varchar(20),
    used        boolean
);

-- 以下为历史遗留的连接鉴权相关表，当前代码未直接引用，保留以与原库结构一致
create table if not exists role
(
    id   serial primary key,
    name varchar(32)
);

create table if not exists "user"
(
    id                      serial primary key,
    username                varchar(32),
    password                varchar(255),
    enabled                 boolean,
    "accountNonExpired"     boolean,
    "accountNonLocked"      boolean,
    "credentialsNonExpired" boolean
);

create table if not exists user_role
(
    id  serial primary key,
    uid int,
    rid int
);
