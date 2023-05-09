#include <libpq-fe.h>
#define CONN_STRING "user=postgres dbname=LhChat"

PGconn* connetti_db(char *connstring);
void disconnetti_db(PGconn *conn);
int insert_utente(char *username, char *password);
int delete_utente(char *username);
int verifica_se_utente_registrato(char *username, char *password);


