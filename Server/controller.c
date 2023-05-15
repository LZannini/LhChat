#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <libpq-fe.h>
#include <time.h>

#include "controller.h"
#include "gestore_richieste.h"
#include "database.h"

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
    produci_risposta_login(LOGINNONTROVATO, risposta);
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
  
  utente_registrato = check_if_registrato(username, password);
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
  
  nome_stanza = (NULL, "|");
  nome_admin = (NULL, "|");
  
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
  if(stanze_trovate == NULL){
    produci_risposta_mie_stanze(VEDISTANZEERR, stanze_trovate, risposta);
  }else if(PQntuples(stanze_trovate) == 0){
    produci_risposta_mie_stanze(STANZENONTROVATE, stanze_trovate, risposta);
  }else{
    produci_risposta_mie_stanze(VEDISTANZEOK, stanze_trovate, risposta);
  }
}

void richiesta_invia_messaggio(char *richiesta, char *risposta){

  int inserito = 0;
  
  char *codice;
  char *mittente;
  char *id_stanza_str;
  char *orario;
  char *testo;
  
  time_t t_orario;
  struct tm tm_orario;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  mittente = strtok(NULL, "|");
  id_stanza_str = strtok(NULL, "|");
  orario = strtok(NULL, "|");
  testo = strtok(NULL, "|");
  
  id_stanza = atoi(id_stanza_str);
  
  memset(&tm_orario, 0, sizeof(struct tm));
  strptime(orario, "%Y-%m-%d %H:%M:%S", &tm_orario);
  t_orario = mktime(&tm_orario);
  
  inserito = insert_messaggio(mittente, id_stanza, t_orario, testo);
  if(inserito == 0){
    produci_risposta_ins_mess(INVIAMESSERR, risposta);
  }else{
    produci_risposta_ins_mess(INVIAMESSOK, risposta);
  }
}

void richiesta_accetta_richiesta(char *richiesta, char *risposta){
  int inserito = 0;
  
  char *codice;
  char *id_stanza_str;
  int id_stanza;
  char *username;
  
  codice = strtok(richiesta, "|");
  id_stanza_str = strtok(NULL, "|");
  id_stanza = atoi(id_stanza_str);
  username = strtok(NULL, "|");
  
  inserito = insert_appartenenza_stanza(username, id_stanza);
  
  if(inserito == 0){
    produci_risposta_new_membro(ACCETTARICERR, risposta);
  }else{
    produci_risposta_new_membro(ACCETTARICOK, risposta);
  }
}
   

void richiesta_apri_chat(char *richiesta, char *risposta){
  PGresult *mess_trovati;
  
  char *codice;
  char *id_stanza_str;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  id_stanza_str = strtok(NULL, "|");
  
  id_stanza = atoi(id_stanza_str);
  
  mess_trovati = select_messaggi_stanza(id_stanza);
  
  if(mess_trovati == NULL){
    produci_risposta_vedi_chat(APRICHATERR, mess_trovati, risposta);
  }else if(PQntuples(mess_trovati) == 0){
    produci_risposta_vedi_chat(CHATVUOTA, mess_trovati, risposta);
  }else{
    produci_risposta_vedi_chat(APRICHATOK, mess_trovati, risposta);
  }
}
  
  
void richiesta_elimina_utente(char *richiesta, char *risposta){
  int eliminato = 0;
  
  char *codice;
  char *username;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL,"|");
  
  eliminato = delete_utente(username);
  if(eliminato == 0){
    produci_risposta_elimina_utente(ELIMINAUSERERR, risposta);
  }else{
    produci_risposta_elimina_utente(ELIMINAUSEROK, risposta);
  }
}


void richiesta_elimina_stanza(char *richiesta, char *risposta){
  int eliminato = 0;
  
  char *codice;
  char *id_stanza_str;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  id_stanza_str = strtok(NULL,"|");
  
  id_stanza = atoi(id_stanza_str);
  
  eliminato = delete_stanza(id_stanza);
  if(eliminato == 0){
    produci_risposta_elimina_stanza(ELIMINASTANZAERR, risposta);
  }else{
    produci_risposta_elimina_stanza(ELIMINASTANZAOK, risposta);
  }
}

void richiesta_abbandona_stanza(char *richiesta, char *risposta){
  int eliminato = 0;
  
  char *codice;
  char *id_stanza_str;
  char *username;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  id_stanza_str = strtok(NULL,"|");
  username = strtok(NULL, "|");
  
  id_stanza = atoi(id_stanza_str);
  
  eliminato = delete_appartenenza_stanza(username, id_stanza);
  if(eliminato == 0){
    produci_risposta_abbandona_stanza(ESCIDASTANZAERR, risposta);
  }else{
    produci_risposta_abbandona_stanza(ESCIDASTANZAOK, risposta);
  }
}  

void richiesta_cerca_stanza(char *richiesta, char *risposta){
  PGresult *stanze_trovate;
  
  char *codice;
  char *nome_stanza;
  
  codice = strtok(richiesta, "|");
  nome_stanza = strtok(NULL, "|");
  
  stanze_trovate = check_if_stanza_esiste(nome_stanza);
  if(stanze_trovate == NULL){
    produci_risposta_cerca_stanze(CERCASTANZAERR, stanze_trovate, risposta);
  }else if(PQntuples(stanze_trovate) == 0){
    produci_risposta_cerca_stanze(STANZENONTROVATE, stanze_trovate, risposta);
  }else{
    produci_risposta_cerca_stanze(CERCASTANZAOK, stanze_trovate, risposta);
  }
}

void richiesta_modifica_password(char *richiesta, char *risposta){
  int modificata = 0;
  
  char *codice;
  char *username;
  char *nuova_pass;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  nuova_pass = strtok(NULL, "|");
  
  modificata = update_password(username, nuova_pass);
  if(modificata == 0){
    produci_risposta_up_password(MODPASSERR, risposta);
  }else(modifata == 1){
    produci_risposta_up_password(MODPASSOK, risposta);
  }
}

void richiesta_modifica_password(char *richiesta, char *risposta){
  int modificato = 0;
  
  char *codice;
  char *username;
  char *nuovo_user;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  nuovo_user = strtok(NULL, "|");
  
  modificato = update_username(username, nuovo_user);
  if(modificato == 0){
    produci_risposta_up_user(MODUSERERR, risposta);
  }else(modifato == 1){
    produci_risposta_up_user(MODUSEROK, risposta);
  }
}

void richiesta_inserisci_richiesta(char *richiesta, char *risposta){
  int inserito = 0;
  
  char *codice;
  char *username;
  char *id_stanza_str;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  id_stanza_str = strtok(NULL, "|");
  id_stanza = atoi(id_stanza_str);
  
  inserito = insert_richiesta_stanza(username, id_stanza);
  if(inserito == 0){
    produci_risposta_ins_ric(RICHIESTASTANZAERR, risposta);
  }else{
    produci_risposta_ins_ric(RICHIESTASTANZAOK, risposta);
  }
}

void richiesta_visualizza_richieste(char *richiesta, char *risposta){
  PGresult *richieste_trovate;
  
  char *codice;
  char *id_stanza_str;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  id_stanza_str(NULL, "|");
  id_stanza = atoi(id_stanza_str);
  
  richieste_trovate = select_richieste_stanza(id_stanza);
  if(richieste_trovate == NULL){
    produci_risposta_vedi_ric(VEDIRICHIESTEERR, richieste_trovate, risposta);
  }else if(PQntuples(richieste_trovate) == 0){
    produci_risposta_vedi_ric(NORICHIESTE, richieste_trovate, risposta);
  }else{
    produci_risposta_vedi_ric(VEDIRICHIESTE, richieste_trovate, risposta);
  }
}

void richiesta_verifica_admin(char *richiesta, char *risposta){
  PGresult *admin_trovato
  
  char *codice;
  char *username;
  char *id_stanza_str;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  username = strtok(NULL, "|");
  id_stanza_str(NULL, "|");
  id_stanza = atoi(id_stanza_str);
  
  admin_trovato = check_if_admin(username, id_stanza);
  if(admin_trovato == NULL){
    produci_risposta_admin(ADMINERR, admin_trovato, risposta);
  }else if(PQntuples(admin_trovato) == 0){
    produci_risposta_admin(ADMINNO, admin_trovato, risposta);
  }else{
    produci_risposta_admin(ADMINSI, admin_trovato, risposta);
  }
}

void richiesta_vedi_partecipanti(char *richiesta, char *risposta){
  PGresult *partecipanti;
  
  char *codice;
  char *id_stanza_str;
  int id_stanza;
  
  codice = strtok(richiesta, "|");
  id_stanza_str = strtok(NULL, "|");
  id_stanza = atoi(id_stanza_str);
  
  partecipanti = select_partecipanti(id_stanza);
  if(partecipanti == NULL){
    produci_risposta_partecipanti(VEDIPARTERR, partecipanti, risposta);
  }else if(PQntuples(partecipanti)) == 0){
    produci_risposta_partecipanti(NOPART, partecipanti, risposta);
  }else{
    produci_risposta_partecipanti(VEDIPARTOK, partecipanti, risposta);
  }
}
  

void gestisci_richiesta_client(char *richiesta, char *risposta){
  int cod_comando;
  char *comando;
  risposta = malloc(256);
  
  comando = strtok(richiesta, "|");
  cod_comando = atoi(comando);
  
  if(cod_comando == LOGIN){
    richiesta_login(richiesta, risposta);
  }else if(cod_comando == REG){
    richiesta_registrazione(richiesta, risposta);
  }else if(cod_comando == CREASTANZA){
    richiesta_new_stanza(richiesta, risposta);
  }else if(cod_comando == VEDISTANZE){
    richiesta_vedi_stanze(richiesta, risposta);
  }else if(cod_comando == INVIAMESS){
    richiesta_invia_messaggio(richiesta, risposta);
  }else if(cod_comando == ACCETTARIC){
    richiesta_accetta_richiesta(richiesta, risposta);
  }else if(cod_comando == APRICHAT){
    richiesta_apri_chat(richiesta, risposta);
  }else if(cod_comando == ELIMINAUSER){
    richiesta_elimina_utente(richiesta, risposta);
  }else if(cod_comando == ELIMINASTANZA){
    richiesta_elimina_stanza(richiesta, risposta);
  }else if(cod_comando == ESCIDASTANZA){
    richiesta_abbandona_stanza(richiesta, risposta);
  }else if(cod_comando == CERCASTANZA){
    richiesta_cerca_stanza(richiesta, risposta);
  }else if(cod_comando == MODPASS){
    richiesta_modifica_password(richiesta, risposta);
  }else if(cod_comando == MODUSER){
    richiesta_modifica_username(richiesta, risposta);
  }else if(cod_comando == RICHIESTASTANZA){
    richiesta_inserisci_richiesta(richiesta, risposta);
  }else if(cod_comando == VEDIRICHIESTE){
    richiesta_visualizza_richieste(richiesta, risposta);
  }else if(cod_comando == ADMIN){
    richiesta_verifica_admin(richiesta, risposta);
  }else if(cod_comando == VEDIPART){
    richiesta_vedi_partecipanti(richiesta, risposta);
  }
}
