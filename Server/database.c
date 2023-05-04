#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>
#include "database.h"

PGconn* connetti_db(){
    PGconn *conn = PQconnectdb("user=postgres dbname=LhChat");

    if (PQstatus(conn) == CONNECTION_BAD) {

        fprintf(stderr, "Connessione al database fallita: %s\n",
            PQerrorMessage(conn));
        do_exit(conn);
    }

    PQfinish(conn);
}

void disconnetti_db(PGconn *conn) {
    PQfinish(conn);
    exit(1);
}

int insert_utente(char *username, char *password){
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024];
    char error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "Insert into utente(username, password) values ($$%s$$, $$%s$$)", username, password);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(query));
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

    disconnetti_db(miaconn);
    return out;
}

int delete_utente(char *username){
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024];
    char error[1024];
    int out = 0;

    if (conn != NULL){
        sprintf(query, "delete from utente where username = $$%s$$", username);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(query));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore nell'eliminazione dell'utente. \n");
        }
        else{
            printf("DB: Utente eliminato con successo.\n");
            flag = 1;
        }
        PQclear(query);
    }
    else
        printf("DB: Errore! Connessione al database fallita.\n");

    disconnetti_db(miaconn);
    return out;
}

PGresult *check_if_signup(char *username, char *password){
    PGconn *conn = PQconnectdb(CONN_STRING);
    PGresult *res;
    char query[1024];
    char error[1024];

    if(conn != NULL){
        sprintf(query, "select * from utente WHERE username = $$%s$$ AND password = $$%s$$", username, password);
        res = PQexec(conn, query);
        strcpy(error, PQresultErrorMessage(res));
        if(strlen(error) > 0){
            printf("%s\n", error);
            printf("DB: Errore! Utente non trovato. \n");
            PQclear(res);
            res = NULL;
        }
    }
    else
        printf("DB: Errore! Connessione al database fallita. \n");


    disconnetti_db(miaconn);
    return utente_registrato_db;


}




