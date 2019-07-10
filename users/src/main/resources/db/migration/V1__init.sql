create table accounts (
    id UUID not null,
    bank_id UUID not null,
    currency varchar(255),
    number varchar(255),
    user_id UUID,
    primary key (id)
);

create table recipients (
    id UUID not null,
    account_number varchar(255),
    bank_address varchar(255),
    bank_name varchar(255),
    bank_urn varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    user_id UUID,
    primary key (id)
);

create table user_details (
    id UUID not null,
    active boolean not null,
    address varchar(255),
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    phone_number varchar(255),
    secret_hash varchar(255),
    user_group varchar(255) not null,
    primary key (id)
);

create table users (
    id UUID not null,
    user_details_id UUID not null,
    primary key (id)
);

alter table accounts
    add constraint FK_accounts_users
        foreign key (user_id)
            references users;

alter table recipients
    add constraint FK_recipients_users
        foreign key (user_id)
            references users;

alter table users
    add constraint FK_users_user_details
        foreign key (user_details_id)
            references user_details;