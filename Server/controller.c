#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "controller.h"
#include "gestore_richieste.h"

void richiesta_registrazione(char *richiesta){
  int utente_registrato;
  int inserito = 0;
  
  char *codice;
  char *username;
  char *password;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  password = strtok(NULL, "|");
  
  utente_registrato = verifica_se_utente_registrato(nome, password);
  if(utente_registrato == 0){         
    //invia risposta al client
  }else{
    inserito = insert_utente(username, password);
    if(inserito == 0){
      //errore, fallo sapere al client
    }else{
      //aggiorna e reinvia la risposta al client
    }
  }
}
    
  

void gestisci_richiesta_client(char *richiesta){
  int cod_comando;
  char *comando;
  
  comando = strtok(richiesta, "|");
  cod_comando = atoi(comando);
  
  if(comando == REG){
    richiesta_registrazione(richiesta);
