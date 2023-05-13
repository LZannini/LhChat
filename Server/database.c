#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>
#include "database.h"

PGconn* connetti(){
    PGconn *conn = PQconnectdb("user=postgres dbname=LhChat");

    if (PQstatus(conn) == CONNECTION_BAD) {

        fprintf(stderr, "Connessione al database fallita: %s\n",
            PQerrorMessage(conn));
        do_exit(conn);
    }

    PQfinish(conn);
}

void disconnetti(PGconn *conn) {
    PQfinish(conn);
    exit(1);
}

PGresult *select_stanze_utente(char *username){
    PGconn *miaconn = PQconnectdb(CONN_STRING);;
    PGresult *res;
    char query[1024], error[1024];

    if(conn != NULL)
    {
        sprintf(query, "select s.id_stanza, s.nome_stanza from stanza s join appartenenza_stanza a on s.id_stanza=a.id_stanza where a.username = $$%s$$", username);
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
    PGconn *conn = PQconnectdb(CONN_STRING);
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

int insert_utente(char *username, char *password){
    PGconn *conn = PQconnectdb(CONN_STRING);
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
            flag = 1;
        }
        PQclear(query);
    }
    else
        printf("DB: Errore! Connessione al database fallita. \n");

    disconnetti(conn);
    return out;
}

int insert_stanza(int id_stanza, char *nome_stanza, char *nome_admin){
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "insert into stanza(id_stanza, nome_stanza, nome_admin) values ($$%d$$, $$%s$$, $$%s$$)", id_stanza, nome_stanza, nome_admin);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'inserimento della stanza. \n");
        }
        else{
            printf("DB: Stanza aggiunta con successo.")
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
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "insert into appartenenza_stanza(username, id_stanza) values ($$%d$$, $$%d$$)", username, id_stanza);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'inserimento dell'utente nella stanza. \n");
        }
        else{
            printf("DB: Utente aggiunto alla stanza con successo.")
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
    PGconn *conn = PQconnectdb(CONN_STRING);
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
        PQclear(query);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int delete_utente(char *username){
    PGconn *conn = PQconnectdb(CONN_STRING);
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
        PQclear(query);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int insert_messaggio(char *mittente, int id_stanza, time_t *ora_invio, char *testo){
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "insert into messaggio(mittente, id_stanza, ora_invio, testo) values ($$%s%%, $$%d$$, $$%s%%, $$%ld$$)", mittente, id_stanza, ora_invio, testo);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if (strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'inserimento del messaggio. \n");
        }
        else{
            printf("DB: Messaggio aggiunto con successo.")
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
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "delete from appartenenza_stanza where username = $$%s$$ and id_stanza = $$%d&&", username, id_stanza);
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
        PQclear(query);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti(conn);
    return out;
}

int check_if_registrato(char *username, char *password){
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024];
    int val;

    if(conn != NULL){
        sprintf(query, "select * from utente WHERE username = $$%s$$ AND password = $$%s$$", username, password);
        res = PQexec(conn, query);

        if(PQresultStatus(res) == PGRES_TUPLES_OK && PQntuples(res) == 0){
            val = 1;
        }else if(PQresultStatus(res) == PGRES_TUPLES_OK && PQntuples > 0){
            val = 0;
        }else{
            val = 2; //perchè c'è stato un errore
    }
    else
        printf("DB: Errore! Connessione al database fallita. \n");


    disconnetti(conn);
    return val;
}

PGresult *check_if_stanza_esisite(char *nome_stanza){
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024], error[1024];

    if(conn != NULL){
        sprintf(query, "select * from stanza WHERE nome_stanza LIKE $$%%s%$", nome_stanza);
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




