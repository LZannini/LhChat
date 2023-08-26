//per compilare => gcc -o wbc server.c controller.c gestore_richieste.c database.c -I/usr/include/postgresql -L/usr/lib/postgresql/15/lib -lpq

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <pthread.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <string.h>

#include "controller.h"
#include "gestore_richieste.h"


#define PORTA 5000
#define MAX_USERS 100

typedef struct {
    int id_stanza;
    int socket_fd;
    char username[200];
} OnlineUser;

OnlineUser onlineUsers[MAX_USERS];
    int onUsersCount;

    void *gestisci(void *arg){
    int sock = *(int *) arg;
    int read_size;
    int cod_comando;
    char buffer[4096];
    char risposta[4096];
    
    while ((read_size = recv(sock, buffer, 2000, 0)) > 0) {
    if (read_size < 0) {
        perror("Server: errore durante la lettura dei dati dal socket");
        break;
    }else{
        printf("Server: richiesta ricevuta con successo!\n");
    }
    
    printf("\tRichiesta del client: %s\n", buffer);
    cod_comando = gestisci_richiesta_client(buffer, &risposta, sock);
    if (risposta != NULL) {
    	printf("\tRisposta del server: %s\n", risposta);
        send(sock, risposta, strlen(risposta), 0);
    } else {
        printf("Server: errore durante il recupero delle informazioni richieste");
    }
    }
    if (cod_comando != APRICHAT) {
        close(*(int *) arg);
    }
    pthread_exit(0);
}

void aggiungiUtente(int socket_fd, int id_stanza, char *username){
    if (onUsersCount < MAX_USERS) {
        onlineUsers[onUsersCount].socket_fd = socket_fd;
        onlineUsers[onUsersCount].id_stanza = id_stanza;
        strcpy(onlineUsers[onUsersCount].username, username);
        onUsersCount++;
    } else {
        printf("Si è verificato un errore!\n");
    }
}

void mandaMessaggio(char *username, char *notifica, int id_stanza) {
    for (int i = 0; i < onUsersCount; i++) {
        if (onlineUsers[i].id_stanza == id_stanza && strcmp(username, onlineUsers[i].username) != 0) {
            if(send(onlineUsers[i].socket_fd, notifica, strlen(notifica), 0) > 0){
                printf("funziona\n");
            } else {
                printf("non funziona\n");
            }
        }
    }
}

int chiudiConnessione(char *username) {
	int trovato = 0;
	int i;
	
	for(i = 0; i < onUsersCount; i++) {
		if(strcmp(onlineUsers[i].username, username) == 0) {
			trovato = 1;
			close(onlineUsers[i].socket_fd);
			break;
		}
	}
	
	if(trovato) {
		for(; i < onUsersCount; i++) {
			onlineUsers[i] = onlineUsers[i+1];
		}
		
		onUsersCount--;
	}
	return trovato;
}
  

int main(){
    int s_fd, c_fd, len, err;
    struct sockaddr_in sin, client;
    char mess[256];
    pthread_t tid;

    
    //crea socket
    s_fd = socket(AF_INET, SOCK_STREAM, 0);
    if(s_fd < 0){
        perror("Errore durante la creazione del server socket");
        exit(1);
    }else{
        printf("Server: socket creata con successo!\n");
    }
    
    //imposta indirizzo
    sin.sin_family = AF_INET;
    sin.sin_port = htons(PORTA);
    sin.sin_addr.s_addr = htonl(INADDR_ANY);
    
    len = sizeof(client);
    
    //effettua il bind
    if(bind(s_fd, (struct sockaddr *) &sin, sizeof(sin)) != 0){
        perror("Errore nel bind del server socket");
        exit(1);
    }else{
        printf("Server: bind riuscito!\n");
    }
    
    //mettiti in ascolto
    if(listen(s_fd, 10) < 0){
        perror("Errore durante l'ascolto del socket server");
        exit(1);
    }else{
        printf("Server: in ascolto!\n");
    }
    int i=0;
    
    //accetta una nuova connessione
    while(1){

        c_fd = accept(s_fd, (struct sockaddr *) &client, &len);
        if (c_fd < 0) {
            perror("Errore durante l'accettazione della connessione");
            exit(1);
        }else{
            printf("Server: connessione accettata!\n");
        }
        
        inet_ntop(AF_INET, &client.sin_addr, mess, sizeof(mess));
        
        err = pthread_create(&tid, NULL, gestisci, (void *) &c_fd);
        if(err != 0){
            printf("Impossibile creare il Thread, %s\n", strerror(err));
            pthread_detach(tid);
        }
    }
    
    close(s_fd);
    return 0;
}
