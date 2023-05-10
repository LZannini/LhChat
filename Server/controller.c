#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "controller.h"
#include "gestore_richieste.h"

void richiesta_login(char *richiesta, char *risposta){
  int trovato = 0;
  int utente_registrato;
  
  char *codice;
  char *username;
  char *password;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  password = strtok(NULL, "|");
  
  utente_registrato = check_if_registrato(username, password);
  if(utente_registrato == 0){
    produci_risposta_login(LOGINOK, risposta);
  }else if(utente_registrato == 1){
    produci_risposta_login(LOGINNONTROVATO, risposta)
  }else{
    produci_risposta_login(LOGINERR, risposta);
  }
}

void richiesta_registrazione(char *richiesta, char *risposta){
  int utente_registrato;
  int inserito = 0;
  
  char *codice;
  char *username;
  char *password;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  password = strtok(NULL, "|");
  
  utente_registrato = check_if_registrato(nome, password);
  if(utente_registrato == 0){         
    produci_risposta_registrazione(GIAREGISTRATO, risposta, username);
  }else{
    inserito = insert_utente(username, password);
    if(inserito == 0){
      produci_risposta_registrazione(REGERR, risposta, username);
    }else{
      produci_risposta_registrazione(REGOK, risposta, username);
    }
  }
}

void richiesta_new_stanza(char *richiesta, char *risposta){
  int inserito = 0;
  
  char *codice;
  int id_stanza;
  char *id_stanza_str;
  char *nome_stanza;
  char *nome_admin;
  
  codice = strtok(richiesta, "|");
  id_stanza_str = strtok(NULL, "|");
  id_stanza = atoi(id_stanza_str);
  
  nome_stanza(NULL, "|");
  nome_admin(NULL, "|");
  
  inserito = insert_stanza(id_stanza, nome_stanza, nome_admin);
  if(inserito == 0){
    produci_risposta_new_stanza(CREASTANZAERR, risposta);
  }else{
    produci_risposta_new_stanza(CREASTANZAOK, risposta);
  }
}

void richiesta_vedi_stanze(char *richiesta, char *risposta){
  PGresult *stanze_trovate;
  
  char *codice;
  char *username;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  
  stanze_trovate = select_stanze_utente(username);
  if(res == NULL){
    produci_risposta_mie_stanze(VEDISTANZEERR, stanze_trovate, risposta);
  }else if(PQntuples(res) == 0){
    produci_risposta_mie_stanze(STANZENONTROVATE, stanze_trovate, risposta);
  }else{
    produci_risposta_mie_stanze(VEDISTANZEOK, stanze_trovate, risposta);
  }
}
   
  

void gestisci_richiesta_client(char *richiesta, char *risposta){
  int cod_comando;
  char *comando;
  risposta = malloc(256);
  
  comando = strtok(richiesta, "|");
  cod_comando = atoi(comando);
  
  if(comando == LOGIN){
    richiesta_login(richiesta, risposta);
  }else if(comando == REG){
    richiesta_registrazione(richiesta, risposta);
  }else if(comando == CREASTANZA){
    richiesta_new_stanza(richiesta, risposta);
  }else if(comando == VEDISTANZE){
    richiesta_vedi_stanze(richiesta, risposta);
