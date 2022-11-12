CREATE TABLE public.clients (
    id bigserial NOT NULL,
    login varchar(100) NOT NULL,
    last_name varchar(100) NULL,
    first_name varchar(100) NULL,
    patronymic varchar(100) NULL,
    birth_date date NULL,
    created_at timestamp with time zone NULL,
    updated_at timestamp with time zone NULL,
    CONSTRAINT clients_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX clients_login_idx ON public.clients (login);


CREATE TABLE public.messages (
     id bigserial NOT NULL,
     body text NOT NULL,
     created_at timestamptz NULL,
     client_id int8 NOT NULL,
     CONSTRAINT messages_pk PRIMARY KEY (id)
);

ALTER TABLE public.messages ADD CONSTRAINT messages_clients_fk FOREIGN KEY (client_id) REFERENCES public.clients(id);

CREATE TABLE public.shedlock (
     "name" varchar(64) NOT NULL,
     lock_until timestamp NOT NULL,
     locked_at timestamp NOT NULL,
     locked_by varchar(255) NOT NULL,
     CONSTRAINT shedlock_pkey PRIMARY KEY (name)
);