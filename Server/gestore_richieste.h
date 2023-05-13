#ifndef GESTORE_RICHIESTE_H
#define GESTORE_RICHIESTE_H

#include <libpq-fe.h>

// comandi client
#define LOGIN 001
#define REG 002
#define CREASTANZA 003
#define CERCASTANZA 004
#define INVIAMESS 005
#define APRICHAT 006
#define ACCETTARIC 007
#define MODPASS 009
#define MODUSER 010
#define VEDISTANZE 011
#define VEDIPART 012
#define ELIMINAUSER 013
#define ELIMINASTANZA 014
#define ESCIDASTANZA 015
// comandi OK server
#define LOGINOK 101
#define REGOK 102
#define CREASTANZAOK 103
#define CERCASTANZAOK 104
#define INVIAMESSOK 105
#define APRICHATOK 106
#define ACCETTARICOK 107
#define MODPASSOK 108
#define MODUSEROK 109
#define VEDISTANZEOK 110
#define VEDIPARTOK 111
#define ELIMINAUSEROK 112
#define ELIMINASTANZAOK 113
#define ESCIDASTANZAOK 114
// comandi ERR server
#define LOGINERR 201
#define REGERR 202
#define CREASTANZAERR 203
#define CERCASTANZAERR 204
#define INVIAMESSERR 205
#define APRICHATERR 206
#define ACCETTARICERR 207
#define MODPASSERR 208
#define MODPASSUSER 209
#define VEDISTANZEERR 210
#define VEDIPARTERR 211
#define ELIMINAUSERERR 212
#define ELIMINASTANZAERR 213
#define ESCIDASTANZAERR 214
// altri errori
#define LOGINNONTROVATO 301
#define GIAREGISTRATO 302
#define STANZAGIAESISTE 303
#define STANZENONTROVATE 310
#define CHATVUOTA 306


// funzioni per creare le risposte da inviare al client
void produci_risposta_registrazione(const int comando, char *risposta, char *username);
void produci_risposta_login(const int comando, char *risposta);
void produci_risposta_new_stanza(const int comando, char *risposta);
void produci_risposta_mie_stanze(const int comando, PQresult *res, char *risposta);
void produci_risposta_new_membro(const int comando, char *risposta);
void produci_risposta_ins_mess(const int comando, char *risposta);
void produci_risposta_vedi_chat(const int comando, PQresult *res, char *risposta);
void produci_risposta_elimina_utente(const int comando, char *risposta);
void produci_risposta_elimina_stanza(const int comando, char *risposta);
void produci_risposta_abbandona_stanza(const int comando, char *risposta)

#endif
