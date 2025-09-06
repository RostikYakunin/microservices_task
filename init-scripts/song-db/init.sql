create table songs
(
    id       bigint       not null,
    album    varchar(100) not null,
    artist   varchar(100) not null,
    duration varchar(5)   not null,
    name     varchar(100) not null,
    year     varchar(4)   not null,
    primary key (id)
)