#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libpq-fe.h>
#include "database.h"

PGconn* connetti(char *conn_string){
    PGconn *conn = PQconnectdb(conn_string);

    if (PQstatus(conn) == CONNECTION_BAD) {

        fprintf(stderr, "Connessione al database fallita: %s\n",
            PQerrorMessage(conn));
        disconnetti(conn);
        return NULL;
    }

    return conn;
}

void disconnetti(PGconn *conn) {
    PQfinish(conn);
    printf("Server: db disconnesso\n\n");
}

PGresult *select_stanze_utente(char *username){
    PGconn *conn = connetti(CONN_STRING);;
    PGresult *res;
    char query[1024], error[1024];

    if(conn != NULL)
    {
        sprintf(query, "select s.id_stanza, s.nome_stanza, s.nome_admin from stanza s join appartenenza_stanza a on s.id_stanza=a.id_stanza where a.username = $$%s$$", username);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore select: stanze non trovate.\n");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return res;
}

PGresult *select_stanze(char *username){
    PGconn *conn = connetti(CONN_STRING);;
    PGresult *res;
    char query[1024], error[1024];

    if(conn != NULL)
    {
        sprintf(query, "select id_stanza, nome_stanza, nome_admin from stanza where id_stanza not in (select id_stanza from appartenenza_stanza where username = $$%s$$)", username);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore select: stanze non trovate.\n");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return res;
}

PGresult *select_messaggi_stanza(int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];

    if (conn != NULL){
        sprintf(query, "select mittente, ora_invio, testo from messaggio where id_stanza = $$%d$$ ORDER BY ora_invio", id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore select: messaggi non trovati.\n");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return res;
}

PGresult *select_richieste_stanza(int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];

    if (conn != NULL){
        sprintf(query, "select utente from richiesta_stanza where id_stanza = $$%d$$", id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore sulla ricerca di richieste per la stanza");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return res;
}

PGresult *select_partecipanti(int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];

    if (conn != NULL){
        sprintf(query, "select * from appartenenza_stanza where id_stanza = $$%d$$", id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore sulla ricerca di richieste per la stanza");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return res;
}

int insert_utente(char *username, char *password){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "Insert into utente(username, password) values ($$%s$$, $$%s$$)", username, password);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'inserimento dell'utente. \n");
        }
        else{
            printf("DB: Utente inserito con successo.\n");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita. \n");

    disconnetti(conn);
    return out;
}

int insert_stanza(char *nome_stanza, char *nome_admin){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "insert into stanza(nome_stanza, nome_admin) values ($$%s$$, $$%s$$)", nome_stanza, nome_admin);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'inserimento della stanza. \n");
        }
        else{
            printf("DB: Stanza aggiunta con successo.");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int insert_appartenenza_stanza(char *username, int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "insert into appartenenza_stanza(username, id_stanza) values ($$%s$$, $$%d$$)", username, id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'inserimento dell'utente nella stanza. \n");
        }
        else{
            printf("DB: Utente aggiunto alla stanza con successo.");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int insert_richiesta_stanza(char *username, int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "insert into richiesta_stanza(utente, id_stanza) values ($$%s$$, $$%d$$)", username, id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nella richiesta dell'utente nella stanza. \n");
        }
        else{
            printf("DB: Richiesta effettuata con successo.");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}


int delete_stanza(int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "delete from stanza where id_stanza = $$%d$$", id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'eliminazione della stanza. \n");
        }
        else{
            printf("DB: Stanza eliminata con successo.\n");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int delete_utente(char *username){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "delete from utente where username = $$%s$$", username);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'eliminazione dell'utente. \n");
        }
        else{
            printf("DB: Utente eliminato con successo.\n");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int insert_messaggio(char *mittente, int id_stanza, time_t ora_invio, char *testo){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "insert into messaggio(mittente, id_stanza, ora_invio, testo) values ($$%s$$, $$%d$$, to_timestamp($$%ld$$), $$%s$$)", mittente, id_stanza, ora_invio, testo);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'inserimento del messaggio. \n");
        }
        else{
            printf("DB: Messaggio aggiunto con successo.");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int delete_appartenenza_stanza(char *username, int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "delete from appartenenza_stanza where username = $$%s$$ and id_stanza = $$%d$$", username, id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'eliminazione dell'utente dalla stanza. \n");
        }
        else{
            printf("DB: Utente eliminato dalla stanza con successo.\n");
            out = 1;
        }
        PQclear(res);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int update_password(char *username, char *nuova_pass){
  PGconn *conn = connetti(CONN_STRING);
  PGresult *res;
  char query[1024], error[1024];
  int out = 0;

  if (conn != NULL){
    sprintf(query, "update utente set password = $$%s$$ where username = $$%s$$", nuova_pass, username);
    res = PQexec(conn, query);
    strcpy(error, PQresultErrorMessage(res));
    if(strlen(error) > 0){
      printf("%s\n", error);
      printf("DB: Errore nella modifica della password dell'utente. \n");
    }
    else{
      printf("DB: Password dell'utente aggiornata con successo.\n");
      out = 1;
    }
    PQclear(res);
  }
  else
    printf("DB: Errore! Connessione al database fallita.\n");

  disconnetti(conn);
  return out;
}

int update_username(char *username, char *nuovo_user){
  PGconn *conn = connetti(CONN_STRING);
  PGresult *res;
  char query[1024], error[1024];
  int out = 0;

  if (conn != NULL){
    sprintf(query, "update utente set username = $$%s$$ where username = $$%s$$", nuovo_user, username);
    res = PQexec(conn, query);
    strcpy(error, PQresultErrorMessage(res));
    if(strlen(error) > 0){
      printf("%s\n", error);
      printf("DB: Errore nella modifica della password dell'utente. \n");
    }
    else{
      printf("DB: Password dell'utente aggiornata con successo.\n");
      out = 1;
    }
    PQclear(res);
  }
  else
    printf("DB: Errore! Connessione al database fallita.\n");

  disconnetti(conn);
  return out;
}

int check_if_registrato(char *username, char *password){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024];
    int val;

    if(conn != NULL){
        printf("Server: connessione con il db effettuata con successo\n");
        sprintf(query, "select * from utente WHERE username = $$%s$$ AND password = $$%s$$", username, password);
        res = PQexec(conn, query);

        if(PQresultStatus(res) == PGRES_TUPLES_OK && PQntuples(res) == 0){
            val = 1;
        }else if(PQresultStatus(res) == PGRES_TUPLES_OK && PQntuples > 0){
            val = 0;
        }else{
            val = 2; //perchè c'è stato un errore
            printf("DB: Errore! Connessione al database fallita. \n");
        }
    }

    disconnetti(conn);
    return val;
}

PGresult *check_if_admin(char *username, int id_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];

    if(conn != NULL){
        sprintf(query, "select * from stanza where nome_admin = $$%s$$ AND id_stanza = $$%d$$", username, id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nella ricerca dell'admin. \n");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita. \n");

    disconnetti(conn);
}


PGresult *check_if_stanza_esiste(char *nome_stanza){
    PGconn *conn = connetti(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];

    if(conn != NULL){
        sprintf(query, "select id_stanza, nome_stanza, nome_admin from stanza WHERE nome_stanza LIKE $$%s%$$", nome_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore! Stanza non trovata. \n");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita. \n");

    disconnetti(conn);
    return res;
}



