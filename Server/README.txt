Istruzioni sistema: 

1) Creare un nuovo db e fare un restore con il file "LhChat.sql";

2) tramite shell entrare nella directory "LhChat\Server";

3) compilare il server digitando il seguente comando: gcc -o wbc server.c controller.c gestore_richieste.c database.c -I/usr/include/postgresql -L/usr/lib/postgresql/15/lib -lpq;

4) avviare il server digitando ./wbc nella directory "LhChat\Server";

Per startare il server c'è bisogno di usare lo username "postgres" all'interno della shell.