#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>

#include "gestore_richieste.h"

void produci_risposta_registrazione(const int comando, char *risposta, char *username){
    if(comando == REGOK){
        sprintf(risposta, "%d|L'utente %s registrato con successo", comando, username);
    }else if(comando == REGERR){
        sprintf(risposta, "%d|Errore nella fase di registrazione, riprova", comando);
    }else if(comando == GIAREGISTRATO){
        sprintf(risposta, "%d|L'utente %s è già registrato, prova ad eseguire l'accesso", comando, username);
    }
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
    }else if(comando == CREASTANZAERR){
        sprintf(risposta, "%d|Errore durante la creazione della stanza", comando);
    }
}

void produci_risposta_mie_stanze(const int comando, PGresult *res, char *risposta){
    if(comando == VEDISTANZEOK){
        int righe = PQntuples(res);
        int i, id;
        char *id_str;
        char *tuple = malloc(sizeof(char) *1000);

        tuple[0] = '\0';
        for(i=0; i<righe; i++){
            id_str = PQgetvalue(res, i, 0);
            id = atoi(id_str);
            sprintf(tuple + strlen(tuple), "|%d,%s,%s", id, PQgetvalue(res, i, 1), PQgetvalue(res, i, 2));
        }

        sprintf(risposta, "%d%s", comando, tuple);
        free(tuple);
    }else if(comando == VEDISTANZEERR){
        sprintf(risposta, "%d|Si è verificato un errore durante la visualizzazione delle stanze", comando);
    }else if(comando == STANZENONTROVATE){
        sprintf(risposta, "%d|Non sono presenti stanze", comando);
    }
}

void produci_risposta_all_stanze(const int comando, PGresult *res, char *risposta){
    if(comando == ALLSTANZEOK){
        int righe = PQntuples(res);
        int i, id;
        char *id_str;
        char *tuple = malloc(sizeof(char) *1000);

        tuple[0] = '\0';
        for(i=0; i<righe; i++){
            id_str = PQgetvalue(res, i, 0);
            id = atoi(id_str);
            sprintf(tuple + strlen(tuple), "|%d,%s,%s", id, PQgetvalue(res, i, 1), PQgetvalue(res, i, 2));
        }

        sprintf(risposta, "%d%s", comando, tuple);
        free(tuple);
    }else if(comando == ALLSTANZEERR)
        sprintf(risposta, "%d|Si è verificato un errore durante la visualizzazione delle stanze", comando);
}

void produci_risposta_new_membro(const int comando, char *risposta){
    if(comando == ACCETTARICOK){
        sprintf(risposta, "%d|Membro inserito nella stanza con successo", comando);
    }else if(comando == ACCETTARICERR){
        sprintf(risposta, "%d|Errore durante l'inserimento del membro nella stanza", comando);
    }
    }

    void produci_risposta_ins_mess(const int comando, char *risposta){
    if(comando == INVIAMESSOK){
        sprintf(risposta, "%d|Il messaggio è stato ricevuto dal server", comando);
    }else if(comando == INVIAMESSERR){
        sprintf(risposta, "%d|Errore nell'invio del messaggio al server", comando);
    }
}

void produci_risposta_vedi_chat(const int comando, PGresult *res, char *risposta){
    if(comando == APRICHATOK){
        int righe = PQntuples(res);
        int i;
        char *orario_str;
        time_t orario;
        struct tm tm_orario;
        char risposta_tmp[4096];
        char row_data[4096];
        char orario_formattato[30];

	snprintf(risposta, 4096, "%d", comando);
        risposta_tmp[0] = '\0';
        for(i=0; i<righe; i++){
            orario_str = PQgetvalue(res, i, 1);
            snprintf(row_data, 4096, "|%s£%s£%s", PQgetvalue(res, i, 0), orario_str, PQgetvalue(res, i, 2));
            
            strncat(risposta, row_data, 4096-strlen(risposta) - 1);
        }
    }else if(comando == APRICHATERR){
        sprintf(risposta, "%d|Errore durante il caricamento della chat", comando);
    }else if(comando == CHATVUOTA){
        sprintf(risposta, "%d|Chat vuota", comando);
    }
}

void produci_risposta_abbandona_stanza(const int comando, char *risposta){
    if(comando == ESCIDASTANZAOK){
        sprintf(risposta, "%d|Abbandono della stanza avvenuto con successo", comando);
    }else if(comando == ESCIDASTANZAERR){
        sprintf(risposta, "%d|Errore durante l'uscita dalla stanza", comando);
    }
}

void produci_risposta_cerca_stanze(const int comando, PGresult *res, char *risposta){
    if(comando == CERCASTANZAOK){
        int righe = PQntuples(res);
        int i, id;
        char *id_str;
        char *tuple = malloc(sizeof(char) *1000);

        tuple[0] = '\0';
        for(i=0; i<righe; i++){
            id_str = PQgetvalue(res, i, 0);
            id = atoi(id_str);
            sprintf(tuple + strlen(tuple), "|%d,%s, %s", id, PQgetvalue(res, i, 1), PQgetvalue(res, i, 2));
        }

        sprintf(risposta, "%d%s", comando, tuple);
        free(tuple);
    }else if(comando == CERCASTANZAERR){
        sprintf(risposta, "%d|Si è verificato un errore durante la ricerca della stanza", comando);
    }else if(comando == STANZENONTROVATE){
        sprintf(risposta, "%d|Non è stata trovata alcuna stanza", comando);
    }
}

void produci_risposta_up_password(const int comando, char *risposta){
    if(comando == MODPASSOK){
        sprintf(risposta, "%d|Password modificata con successo", comando);
    }else if(comando == MODPASSERR){
        sprintf(risposta, "%d|Errore durante la modifica della password", comando);
    }
}

void produci_risposta_ins_ric(const int comando, char *risposta){
    if(comando == RICHIESTASTANZAOK){
        sprintf(risposta, "%d|Richiesta effettuata con successo", comando);
    }else if(comando == RICHIESTASTANZAERR){
        sprintf(risposta, "%d|Errore durante la richiesta d'accesso", comando);
    }
}

void produci_risposta_vedi_ric(const int comando, PGresult *res, char *risposta){
    if(comando == VEDIRICHIESTEOK){
        int righe = PQntuples(res);
        int i;
        char *tuple = malloc(sizeof(char) *1000);

        tuple[0] = '\0';
        for(i=0; i<righe; i++){
            sprintf(tuple + strlen(tuple), "|%s", PQgetvalue(res, i, 0));
        }

        sprintf(risposta, "%d%s", comando, tuple);
        free(tuple);
    }else if(comando == VEDIRICHIESTEERR){
        sprintf(risposta, "%d|Errore durante la visualizzazione delle richieste", comando);
    }
}


void produci_risposta_partecipanti(const int comando, PGresult *res, char *risposta){
    if(comando == VEDIPARTOK){
        int righe = PQntuples(res);
        int i;
        char *tuple = malloc(sizeof(char) *1000);

        tuple[0] = '\0';
        for(i=0; i<righe; i++){
            sprintf(tuple + strlen(tuple), "|%s", PQgetvalue(res, i, 0));
        }

        sprintf(risposta, "%d%s", comando, tuple);
        free(tuple);
    }else if(comando == VEDIPARTERR){
        sprintf(risposta, "%d|Errore durante la ricerca dei partecipanti della stanza", comando);
    }
}

void produci_risposta_leave_chat(const int comando, char *risposta){
	if(comando == LEAVECHATOK){
		sprintf(risposta, "%d|Connessione chiusa nel server", comando);
	} else {
		sprintf(risposta, "%d|Errore durante la chiusura della connessione", comando);
	}
}

void produci_risposta_rimuovi_richiesta(const int comando, char *risposta) {
	if(comando == RIFIUTARICOK){
		sprintf(risposta, "%d|Richiesta rifiutata con successo", comando);
	} else {
		sprintf(risposta, "%d|Errore durante l'operazione di rifiuto della richiesta", comando);
	}
}
