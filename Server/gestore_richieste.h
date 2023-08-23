#ifndef GESTORE_RICHIESTE_H
#define GESTORE_RICHIESTE_H

#include <libpq-fe.h>

// comandi client
#define LOGIN 1
#define REG 2
#define CREASTANZA 3
#define CERCASTANZA 4
#define INVIAMESS 5
#define APRICHAT 6
#define ACCETTARIC 7
#define MODPASS 8
#define VEDISTANZE 10
#define VEDIPART 11
#define LEAVECHAT 12
#define ALLSTANZE 13
#define ESCIDASTANZA 14
#define RICHIESTASTANZA 15
#define VEDIRICHIESTE 16
#define RIFIUTARIC 17
// comandi OK server
#define LOGINOK 101
#define REGOK 102
#define CREASTANZAOK 103
#define CERCASTANZAOK 104
#define INVIAMESSOK 105
#define APRICHATOK 106
#define ACCETTARICOK 107
#define MODPASSOK 108
#define VEDISTANZEOK 110
#define VEDIPARTOK 111
#define LEAVECHATOK 112
#define ALLSTANZEOK 113
#define ESCIDASTANZAOK 114
#define RICHIESTASTANZAOK 115
#define VEDIRICHIESTEOK 116
#define RIFIUTARICOK 117
// comandi ERR server
#define LOGINERR 201
#define REGERR 202
#define CREASTANZAERR 203
#define CERCASTANZAERR 204
#define INVIAMESSERR 205
#define APRICHATERR 206
#define ACCETTARICERR 207
#define MODPASSERR 208
#define VEDISTANZEERR 210
#define VEDIPARTERR 211
#define LEAVECHATERR 212
#define ALLSTANZEERR 213
#define ESCIDASTANZAERR 214
#define RICHIESTASTANZAERR 215
#define VEDIRICHIESTEERR 216
#define RIFIUTARICERR 217
// altri errori
#define LOGINNONTROVATO 301
#define GIAREGISTRATO 302
#define CHATVUOTA 306
#define STANZENONTROVATE 310
#define RICHIESTAGIAINVIATA 315


// funzioni per creare le risposte da inviare al client
void produci_risposta_registrazione(const int comando, char *risposta, char *username);
void produci_risposta_login(const int comando, char *risposta);
void produci_risposta_new_stanza(const int comando, char *risposta);
void produci_risposta_mie_stanze(const int comando, PGresult *res, char *risposta);
void produci_risposta_all_stanze(const int comando, PGresult *res, char *risposta);
void produci_risposta_new_membro(const int comando, char *risposta);
void produci_risposta_ins_mess(const int comando, char *risposta);
void produci_risposta_vedi_chat(const int comando, PGresult *res, char *risposta);
void produci_risposta_abbandona_stanza(const int comando, char *risposta);
void produci_risposta_cerca_stanze(const int comando, PGresult *res, char *risposta);
void produci_risposta_up_password(const int comando, char *risposta);
void produci_risposta_ins_ric(const int comando, char *risposta);
void produci_risposta_vedi_ric(const int comando, PGresult *res, char *risposta);
void produci_risposta_partecipanti(const int comando, PGresult *res, char *risposta);
void produci_risposta_leave_chat(const int comando, char *risposta);
void produci_risposta_rimuovi_richiesta(const int comando, char *risposta);

#endif
