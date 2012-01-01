#include <stdio.h>
#include "oauth.h"
#include "xmalloc.h"
#include "sha1.h"

char *oauth_sign_hmac_sha1_raw (const char *m, const size_t ml, const char *k, const size_t kl) {
	sha1nfo s;
	sha1_initHmac(&s, (const uint8_t*) k, kl);
	sha1_write(&s, m, ml);
	unsigned char *digest = sha1_resultHmac(&s);
  return oauth_encode_base64(HASH_LENGTH, digest);
}

char *oauth_sign_hmac_sha1 (const char *m, const char *k) {
  return(oauth_sign_hmac_sha1_raw (m, strlen(m), k, strlen(k)));
}

char *oauth_body_hash_file(char *filename) {
  FILE *F= fopen(filename, "r");
  if (!F) return NULL;

  size_t len=0;
  char fb[BUFSIZ];
	sha1nfo s;
	sha1_init(&s);

  while (!feof(F) && (len=fread(fb,sizeof(char),BUFSIZ, F))>0) {
		sha1_write(&s, fb, len);
	}
  fclose(F);

  unsigned char *dgst = xmalloc(HASH_LENGTH*sizeof(char)); // oauth_body_hash_encode frees the digest..
  memcpy(dgst, sha1_result(&s), HASH_LENGTH);
  return oauth_body_hash_encode(HASH_LENGTH, dgst);
}

char *oauth_body_hash_data(size_t length, const char *data) {
	sha1nfo s;
	sha1_init(&s);
	for (;length--;) sha1_writebyte(&s, *data++);

  unsigned char *dgst = xmalloc(HASH_LENGTH*sizeof(char)); // oauth_body_hash_encode frees the digest..
  memcpy(dgst, sha1_result(&s), HASH_LENGTH);
  return oauth_body_hash_encode(HASH_LENGTH, dgst);
}

char *oauth_sign_rsa_sha1 (const char *m, const char *k) {
	/* NOT RSA/PK11 support */
	return xstrdup("---RSA/PK11-is-not-supported-by-this-version-of-liboauth---");
}

int oauth_verify_rsa_sha1 (const char *m, const char *c, const char *sig) {
	/* NOT RSA/PK11 support */
	return -1; // mismatch , error
}
