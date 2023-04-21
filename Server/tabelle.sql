-- Il file "LhChat.sql" serve per aggiungere il database su PgAdmin usando il comando Restore.

--
-- TABELLA UTENTE
--

CREATE TABLE utente
(
    username character varying(32) COLLATE pg_catalog."default" NOT NULL,
    password character varying(32) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT utente_pkey PRIMARY KEY (username)
)

--
-- TABELLA STANZA
--

CREATE TABLE stanza
(
    id_stanza integer NOT NULL DEFAULT nextval('stanza_id_stanza_seq'::regclass),
    nome_stanza character varying(32) COLLATE pg_catalog."default" NOT NULL,
    nome_admin character varying(32) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT stanza_pkey PRIMARY KEY (id_stanza),
    CONSTRAINT stanza_nome_admin_fkey FOREIGN KEY (nome_admin)
        REFERENCES public.utente (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

--
-- TABELLA MESSAGGIO
--

CREATE TABLE messaggio
(
    mittente character varying(32) COLLATE pg_catalog."default" NOT NULL,
    id_stanza integer NOT NULL DEFAULT nextval('messaggio_id_stanza_seq'::regclass),
    ora_invio date NOT NULL,
    testo text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT messaggio_id_stanza_fkey FOREIGN KEY (id_stanza)
        REFERENCES public.stanza (id_stanza) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT messaggio_mittente_fkey FOREIGN KEY (mittente)
        REFERENCES public.utente (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

--
-- TABELLA APPARTENENZA_STANZA
--

CREATE TABLE appartenenza_stanza
(
    username character varying(32) COLLATE pg_catalog."default" NOT NULL,
    id_stanza integer NOT NULL DEFAULT nextval('appartenenza_stanza_id_stanza_seq'::regclass),
    CONSTRAINT appartenenza_stanza_id_stanza_fkey FOREIGN KEY (id_stanza)
        REFERENCES public.stanza (id_stanza) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT appartenenza_stanza_username_fkey FOREIGN KEY (username)
        REFERENCES public.utente (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

--
-- TABELLA RICHIESTA_STANZA
--

CREATE TABLE richiesta_stanza
(
    utente character varying(32) COLLATE pg_catalog."default" NOT NULL,
    id_stanza integer NOT NULL DEFAULT nextval('richiesta_accesso_id_stanza_seq'::regclass),
    CONSTRAINT richiesta_accesso_id_stanza_fkey FOREIGN KEY (id_stanza)
        REFERENCES public.stanza (id_stanza) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT richiesta_accesso_utente_fkey FOREIGN KEY (utente)
        REFERENCES public.utente (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
