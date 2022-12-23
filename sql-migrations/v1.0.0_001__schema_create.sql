-- Таблица инициатив
create table if not exists PETITIONS (
    PET_ID bigint,
    PET_TITLE varchar(255) not null,
    PET_LEVEL_ID int not null,
    PET_STATUS_ID int not null,
    PET_UPDATE_TIMESTAMP timestamp not null default now(),
    constraint PETITIONS_PK primary key (PET_ID)
);