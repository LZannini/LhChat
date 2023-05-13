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

#define PORTA 10000

void *gestisci(void *arg){
  int sock = *(int *) arg;
  int read_size;
  char buffer[2000];
  char *risposta;
  
  while(read_size = recv(sock, buffer, 2000, 0) > 0){
    gestisci_richiesta_client(buffer, risposta);
    send(sock, risposta, strlen(risposta), 0);
    free(risposta);
  }
  
  close((int *) arg);
  pthread_exit(0);
}
  

int main(){
  int s_fd, c_fd, len;
  struct sockaddr_in sin, client;
  char mess[256];
  pthread_t tid;
  
  //crea socket
  s_fd = socket(AF_INET, SOCK_STREAM, 0);
  
  //imposta indirizzo
  sin.sin_family = AF_INET;
  sin.sin_port = htons(PORTA);
  sin.sin_addr.s_addr = htonl(INADDR_ANY);
  
  len = sizeof(client);
  
  //effettua il bind
  bind(s_fd, (struct sockaddr *) &sin, sizeof(sin));
  
  //mettiti in ascolto
  listen(s_fd, 10);
  
  //accetta una nuova connessione
  while(1){
    c_fd = accept(s_fd, (struct sockaddr *) &client, &len);
    
    inet_ntop(AF_INET, &client.sin_addr, mess, sizeof(mess));
    
    pthread_create(&tid, NULL, gestisci, (void *) c_fd);
    pthread_detach(tid);
  }
  
  close(s_fd);
  return 0;
}
