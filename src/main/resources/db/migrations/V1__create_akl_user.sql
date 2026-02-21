create table if not exists akl_user (
    id bigserial primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    role varchar(20) not null,
    status varchar(20) not null,
    coach_id bigint null,

    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    last_login_at timestamptz null
);

create index if not exists idx_akl_user_role on akl_user(role);
create index if not exists idx_akl_user_coach_id on akl_user(coach_id);
