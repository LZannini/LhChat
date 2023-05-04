#include <libpq-fe.h>
#define CONN_STRING "user=postgres dbname=LhChat"

PGconn* connetti_db(char *connstring);
void disconnetti_db(PGconn *conn);
int insert_utente(char *username, char *password);
int delete_utente(char *username);
PGresult *check_if_signup(char *username, char *password);


