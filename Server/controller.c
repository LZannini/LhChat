#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <libpq-fe.h>
#include <time.h>

#include "server.h"
#include "controller.h"
#include "gestore_richieste.h"
#include "database.h"

void richiesta_login(char *richiesta, char *risposta){
	int trovato = 0;
	int utente_registrato;

	char *username;
	char *password;

	username = strtok(richiesta, "|");
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

	char *username;
	char *password;

	username = strtok(richiesta, "|");
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

	char *nome_stanza;
	char *nome_admin;


	nome_stanza = strtok(richiesta, "|");
	nome_admin = strtok(NULL, "|");

	inserito = insert_stanza(nome_stanza, nome_admin);
	if(inserito == 0){
		produci_risposta_new_stanza(CREASTANZAERR, risposta);
	}else{
		produci_risposta_new_stanza(CREASTANZAOK, risposta);
	}
}

void richiesta_vedi_stanze(char *richiesta, char *risposta){
	PGresult *stanze_trovate;

	char *username;

	username = strtok(richiesta, "|");

	stanze_trovate = select_stanze_utente(username);
	if(stanze_trovate == NULL){
		produci_risposta_mie_stanze(VEDISTANZEERR, stanze_trovate, risposta);
	}else if(PQntuples(stanze_trovate) == 0){
		produci_risposta_mie_stanze(STANZENONTROVATE, stanze_trovate, risposta);
	}else{
		produci_risposta_mie_stanze(VEDISTANZEOK, stanze_trovate, risposta);
	}
}

void richiesta_all_stanze(char *richiesta, char *risposta){
	PGresult *stanze_trovate;

	char *username;
	
	username = strtok(richiesta, "|");

	stanze_trovate = select_stanze(username);
	if(stanze_trovate == NULL){
		produci_risposta_all_stanze(ALLSTANZEERR, stanze_trovate, risposta);
	}else{
		produci_risposta_all_stanze(ALLSTANZEOK, stanze_trovate, risposta);
	}
}

void richiesta_invia_messaggio(char *richiesta, char *risposta, int socket_fd){

	int inserito = 0;

	char *mittente;
	char *id_stanza_str;
	char *orario;
	char *testo;
	char *copia_richiesta = malloc(1000 * sizeof(char));

	time_t t_orario;
	struct tm tm_orario;
	int id_stanza;

	strncpy(copia_richiesta, richiesta, 1000);
	mittente = strtok(richiesta, "|");
	id_stanza_str = strtok(NULL, "|");
	orario = strtok(NULL, "|");
	testo = strtok(NULL, "|");
	id_stanza = atoi(id_stanza_str);

printf("-------------------------------L'ORARIO E: %s", orario);
	memset(&tm_orario, 0, sizeof(struct tm));
	strptime(orario, "%Y-%m-%d %H:%M:%S", &tm_orario);
	tm_orario.tm_isdst = -1; // Imposta il flag DST (Daylight Saving Time) in modo che mktime lo gestisca
	t_orario = mktime(&tm_orario);
	inserito = insert_messaggio(mittente, id_stanza, t_orario, testo);
	
	if(inserito == 0){
		produci_risposta_ins_mess(INVIAMESSERR, risposta);
	}else{
			mandaMessaggio(mittente, copia_richiesta, id_stanza);
		produci_risposta_ins_mess(INVIAMESSOK, risposta);
	}
	
	free(copia_richiesta);
}

void richiesta_accetta_richiesta(char *richiesta, char *risposta){
	int inserito = 0;

	char *id_stanza_str;
	int id_stanza;
	char *username;

	id_stanza_str = strtok(richiesta, "|");
	id_stanza = atoi(id_stanza_str);
	username = strtok(NULL, "|");

	inserito = insert_appartenenza_stanza(username, id_stanza);

	if(inserito == 0){
		produci_risposta_new_membro(ACCETTARICERR, risposta);
	}else{
		produci_risposta_new_membro(ACCETTARICOK, risposta);
	}
}

void richiesta_rifiuta_richiesta(char *richiesta, char *risposta){
	int eliminato = 0;

	char *id_stanza_str;
	int id_stanza;
	char *username;

	id_stanza_str = strtok(richiesta, "|");
	id_stanza = atoi(id_stanza_str);
	username = strtok(NULL, "|");

	eliminato = remove_richiesta_stanza(username, id_stanza);

	if(eliminato == 0){
		produci_risposta_rimuovi_richiesta(RIFIUTARICERR, risposta);
	}else{
		produci_risposta_rimuovi_richiesta(RIFIUTARICOK, risposta);
	}
}


void richiesta_apri_chat(char *richiesta, char *risposta, int socket_fd){
	PGresult *mess_trovati;

	char *username;
	char *id_stanza_str;
	int id_stanza;

	id_stanza_str = strtok(richiesta, "|");
	username = strtok(NULL, "|");
	id_stanza = atoi(id_stanza_str);

	mess_trovati = select_messaggi_stanza(id_stanza);
	
	if(mess_trovati == NULL){
		produci_risposta_vedi_chat(APRICHATERR, mess_trovati, risposta);
	}else if(PQntuples(mess_trovati) == 0){
		produci_risposta_vedi_chat(CHATVUOTA, mess_trovati, risposta);
		aggiungiUtente(socket_fd, id_stanza, username);
	}else{
		produci_risposta_vedi_chat(APRICHATOK, mess_trovati, risposta);
		aggiungiUtente(socket_fd, id_stanza, username);
	}
}


void richiesta_abbandona_stanza(char *richiesta, char *risposta){
	int eliminato = 0;

	char *id_stanza_str;
	char *username;
	int id_stanza;

	id_stanza_str = strtok(richiesta,"|");
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

	char *nome_stanza;
	char *username;

	nome_stanza = strtok(richiesta, "|");
	username = strtok(NULL, "|");

	stanze_trovate = check_if_stanza_esiste(nome_stanza, username);
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

	char *username;
	char *nuova_pass;

	username = strtok(richiesta, "|");
	nuova_pass = strtok(NULL, "|");

	modificata = update_password(username, nuova_pass);
	if(modificata == 0){
		produci_risposta_up_password(MODPASSERR, risposta);
	}else if(modificata == 1){
		produci_risposta_up_password(MODPASSOK, risposta);
	}
}

void richiesta_inserisci_richiesta(char *richiesta, char *risposta){
	int inserito = 0;

	char *username;
	char *id_stanza_str;
	int id_stanza;

	username = strtok(richiesta, "|");
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

	char *id_stanza_str;
	int id_stanza;

	id_stanza_str = strtok(richiesta, "|");
	id_stanza = atoi(id_stanza_str);

	richieste_trovate = select_richieste_stanza(id_stanza);
	if(richieste_trovate == NULL){
		produci_risposta_vedi_ric(VEDIRICHIESTEERR, richieste_trovate, risposta);
	}else{
		produci_risposta_vedi_ric(VEDIRICHIESTEOK, richieste_trovate, risposta);
	}
}


void richiesta_vedi_partecipanti(char *richiesta, char *risposta){
	PGresult *partecipanti;

	char *id_stanza_str;
	int id_stanza;

	id_stanza_str = strtok(richiesta, "|");
	id_stanza = atoi(id_stanza_str);

	partecipanti = select_partecipanti(id_stanza);
	if(partecipanti == NULL){
		produci_risposta_partecipanti(VEDIPARTERR, partecipanti, risposta);
	}else{
		produci_risposta_partecipanti(VEDIPARTOK, partecipanti, risposta);
	}
}


void richiesta_leave_chat(char *richiesta, char *risposta) {
	char *username;
	int trovato = 0;
	
	username = strtok(richiesta, "|");
	
	trovato = chiudiConnessione(username);
	if(trovato) {
		produci_risposta_leave_chat(LEAVECHATOK, risposta);
	} else {
		produci_risposta_leave_chat(LEAVECHATERR, risposta);
	}
}

int gestisci_richiesta_client(char *richiesta, char *risposta, int socket_fd){
	int cod_comando;
	char *comando;
	char *resto_richiesta;

	comando = strtok(richiesta, "|");
	resto_richiesta = strtok(NULL, "");
	cod_comando = atoi(comando);


	if(cod_comando == LOGIN){
		richiesta_login(resto_richiesta, risposta);
	}else if(cod_comando == REG){
		richiesta_registrazione(resto_richiesta, risposta);
	}else if(cod_comando == CREASTANZA){
		richiesta_new_stanza(resto_richiesta, risposta);
	}else if(cod_comando == VEDISTANZE){
		richiesta_vedi_stanze(resto_richiesta, risposta);
	}else if (cod_comando == ALLSTANZE){
		richiesta_all_stanze(resto_richiesta, risposta);
	}else if(cod_comando == INVIAMESS){
		richiesta_invia_messaggio(resto_richiesta, risposta, socket_fd);
	}else if(cod_comando == ACCETTARIC){
		richiesta_accetta_richiesta(resto_richiesta, risposta);
	}else if(cod_comando == APRICHAT){
		richiesta_apri_chat(resto_richiesta, risposta, socket_fd);
	}else if(cod_comando == ESCIDASTANZA){
		richiesta_abbandona_stanza(resto_richiesta, risposta);
	}else if(cod_comando == CERCASTANZA){
		richiesta_cerca_stanza(resto_richiesta, risposta);
	}else if(cod_comando == MODPASS){
		richiesta_modifica_password(resto_richiesta, risposta);
	}else if(cod_comando == RICHIESTASTANZA){
		richiesta_inserisci_richiesta(resto_richiesta, risposta);
	}else if(cod_comando == VEDIRICHIESTE){
		richiesta_visualizza_richieste(resto_richiesta, risposta);
	}else if(cod_comando == RIFIUTARIC){
		richiesta_rifiuta_richiesta(resto_richiesta, risposta);
	}else if(cod_comando == VEDIPART){
		richiesta_vedi_partecipanti(resto_richiesta, risposta);
	}else if(cod_comando == LEAVECHAT){
		richiesta_leave_chat(resto_richiesta, risposta);
	}
	
	return cod_comando;
}
