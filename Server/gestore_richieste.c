#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "gestore_richieste.h"

void produci_risposta_registrazione(const int comando, char *risposta, char *username){
  if(comando == REGOK){
    sprintf(risposta, "%d|L'utente %s registrato con successo", comando, username);
  }else if(comando == REGERR){
    sprintf(risposta, "%d|Errore nella fase di registrazione, riprova", comando);
  }else if(comando == GIAREGISTRATO){
    sprintf(risposta, "%d|L'utente %s è già registrato, prova ad eseguire l'accesso", comando, username);
  }
  
  void produci_risposta_login(const int comando, char *risposta){
    if(comando == LOGINOK){
      sprintf(risposta, "%d|Login effettuato con successo", comando);
    }else if(comando == LOGINERR){
      sprintf(risposta, "%d|Errore nella fase di login, riprova", comando);
    }else if(comando == LOGINNONTROVATO){
      sprintf(risposta, "%d|Utente non registrato, effettua prima la registrazione", comando);
    }
  }
  
  void produci_risposta_new_stanza(const int comando, char *risposta){
    if(comando == CREASTANZAOK){
      sprintf(risposta, "%d|Stanza creata con successo", comando);
    else if(comando == CREASTANZAERR){
      sprintf(risposta, "%d|Errore durante la creazione della stanza", comando);
  }
  
  void produci_risposta_mie_stanze(const int comando, PQresult *res, char *risposta){
    if(comando == VEDISTANZEOK){
      int righe = PQntuples(res);
      int i, id;
      char *id_str;
      char *tuple = malloc(sizeof(char) *1000);
      
      for(i=0; i<righe; i++){
        id_str = PQgetvalue(res, i, 0);
        id = atoi(id_str);
        sprintf(tuple + strlen(tuple), "|%d,%s", id, PQgetvalue(i, 1));
      }
      
      sprintf(risposta, "%d%s", comando, tuple);
      free(tuple);
    }else if(comando == VEDISTANZEERR){
      sprintf(risposta, "%d|Si è verificato un errore durante la visualizzazione delle stanze", comando);
    }else if(comando == STANZENONTROVATE){
      sprintf(risposta, "%d|Non sono presenti stanze", comando);
    }
}
