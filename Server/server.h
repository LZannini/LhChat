#ifndef SERVER_H
#define SERVER_H

void aggiungiUtente(int socketFd, int id_stanza, const char *username);
void mandaMessaggio(const char *username, const char *notifica, int id_stanza);
int chiudiConnessione(const char *username);

#endif
