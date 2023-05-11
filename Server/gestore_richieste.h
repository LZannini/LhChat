#ifndef GESTORE_RICHIESTE_H
#define GESTORE_RICHIESTE_H

  

// comandi client
#define LOGIN 001
#define REG 002
#define CREASTANZA 003
#define CERCASTANZA 004
#define INVIAMESS 005
#define ACCETTARIC 006
#define RIFIUTARIC 007
#define MODPASS 008
#define MODUSER 009
#define VEDISTANZE 010
#define VEDIPART 011
// comandi OK server
#define LOGINOK 101
#define REGOK 102
#define CREASTANZAOK 103
#define CERCASTANZAOK 104
#define INVIAMESSOK 105
#define ACCETTARICOK 106
#define RIFIUTARICOK 107
#define MODPASSOK 108
#define MODUSEROK 109
#define VEDISTANZEOK 110
#define VEDIPARTOK 111
// comandi ERR server
#define LOGINERR 201
#define REGERR 202
#define CREASTANZAERR 203
#define CERCASTANZAERR 204
#define INVIAMESSERR 205
#define ACCETTARICERR 206
#define RIFIUTARICERR 207
#define MODPASSERR 208
#define MODPASSUSER 209
#define VEDISTANZEERR 210
#define VEDIPARTERR 211
// altri errori
#define LOGINNONTROVATO 301
#define GIAREGISTRATO 302
#define STANZAGIAESISTE 303
#define STANZENONTROVATE 310


// funzioni per creare le risposte da inviare al client
void produci_risposta_registrazione(const int comando, char *risposta, char *username);
void produci_risposta_login(const int comando, char *risposta);
void produci_risposta_new_stanza(const int comando, char *risposta);
void produci_risposta_mie_stanze(const int comando, PQresult *res, char *risposta);


