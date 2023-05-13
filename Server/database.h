#ifndef DATABASE_H
#define DATABASE_H

#include <libpq-fe.h>
#define CONN_STRING "user=postgres dbname=LhChat"

PGconn *connetti(char *connstring);
void disconnetti(PGconn *conn);
PGresult *select_stanze_utente(char *username);
PGresult *select_messaggi_stanza(int id_stanza);
int insert_utente(char *username, char *password);
int insert_stanza(int id_stanza, char *nome_stanza, char *nome_admin);
int insert_appartenenza_stanza(char *username, int id_stanza);
int insert_messaggio(char *mittente, int id_stanza, time_t *ora_invio, char *testo);
int delete_utente(char *username);
int delete_stanza(int id_stanza);
int delete_appartenenza_stanza(char *username, int id_stanza);
int check_if_registrato(char *username, char *password);
PGresult *check_if_stanza_esisite(char *nome_stanza);

#endif
