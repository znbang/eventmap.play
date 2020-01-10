create table users (
    id text primary key,
    provider int not null,
    uid text not null,
    email text not null,
    name text not null,
    created_at timestamptz not null
);

create unique index users_idx on users (email, uid);

create table user_sessions (
  id text primary key,
  user_id text not null,
  created_at timestamptz not null
);

create table events (
  id text primary key,
  user_id text not null,
  created_at timestamptz not null,
  updated_at timestamptz not null,
  start_date timestamptz not null,
  end_date timestamptz not null,
  name text not null,
  location text not null,
  url text not null,
  detail text not null,
  lng float not null,
  lat float not null,
  zoom smallint not null
);

create index events_date_idx on events (end_date);
create index events_user_idx on events (user_id);
