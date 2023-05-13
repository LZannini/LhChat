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
    printf(risposta, "%d|Login effettuato con successo", comando);
  }else if(comando == LOGINERR){
    printf(risposta, "%d|Errore nella fase di login, riprova", comando);
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

void produci_risposta_new_membro(const int comando, char *risposta){
  if(comando == ACCETTARICOK){
    sprintf(risposta, "%d|Membro inserito nella stanza con successo", comando);
  else if(comando == ACCETTARICERR){
    sprintf(risposta, "%d|Errore durante l'inserimento del membro nella stanza", comando);
  }
}

void produci_risposta_ins_mess(const int comando, char *risposta){
  if(comando == INVIAMESSOK){
    sprintf(risposta, "%d|Il messaggio è stato ricevuto dal server", comando);
  else if(comando == INVIAMESSERR){
    sprintf(risposta, "%d|Errore nell'invio del messaggio al server", comando);
  }
}

void produci_risposta_vedi_chat(const int comando, PQresult *res, char *risposta){
  if(comando == APRICHATOK){
    int righe = PQntuples(res);
    int i;
    char *orario_str;
    time_t orario;
    struct tm tm_orario;
    char *tuple = malloc(sizeof(char) *1000);
    
    for(i=0; i<righe; i++){
      orario_str = PQgetvalue(res, i, 1);
      memset(&tm_orario, 0, sizeof(struct tm));
      strptime(orario_str, "%Y-%m-%d %H:%M:%S", &tm_orario);
      orario = mktime(&tm_orario);
      sprintf(tuple + strlen(tuple), "|%s,%ld,%s", PQgetvalue(res, i, 0), orario, PQgetvalue(res, i, 2));
    }
    
    sprintf(risposta, "%d%s", comando, tuple);
    free(tuple);
  }else if(comando == APRICHATERR){
    sprintf(risposta, "%d|Errore durante il caricamento della chat", comando);
  }else if(comando == CHATVUOTA){
    sprintf(risposta, "%d|Chat vuota", comando);
  }
}

void produci_risposta_elimina_utente(const int comando, char *risposta){
  if(comando == ELIMINAUSEROK){
    sprintf(risposta, "%d|Utente eliminato con successo", comando);
  }else if(comando == ELIMINAUSERERR){
    sprintf(risposta, "%d|Errore durante l'eliminazione dell'utente", comando);
  }
}

void produci_risposta_elimina_stanza(const int comando, char *risposta){
  if(comando == ELIMINASTANZAOK){
    sprintf(risposta, "%d|Stanza eliminata con successo", comando);
  }else if(comando == ELIMINASTANZAERR){
    sprintf(risposta, "%d|Errore durante l'eliminazione della stanza", comando);
  }
}

void produci_risposta_abbandona_stanza(const int comando, char *risposta){
  if(comando == ESCIDASTANZAOK){
    sprintf(risposta, "%d|Abbandono della stanza avvenuto con successo", comando);
  }else if(comando == ESCIDASTANZAERR){
    sprintf(risposta, "%d|Errore durante l'uscita dalla stanza", comando);
  }
}
