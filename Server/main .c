#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>

void do_exit(PGconn *conn) {

    PQfinish(conn);
    exit(1);
}

int main() {

    PGconn *conn = PQconnectdb("user=postgres dbname=LhChat");

    if (PQstatus(conn) == CONNECTION_BAD) {

        fprintf(stderr, "Connessione al database fallita: %s\n",
            PQerrorMessage(conn));
        do_exit(conn);
    }

    PQfinish(conn);

    return 0;
}
