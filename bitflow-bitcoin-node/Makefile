# bitcoind Makefile
# 
# derived from http://ejohn.org/blog/keeping-passwords-in-source-control/
# 
# this script requires that the BITCOIN_FLOW_PASSWORD environment variable be set

.PHONY: decrypt_properties encrypt_properties build clean

SECURE_PROPERTIES=conf/bitcoind.secure.properties
BITCOIND_IMAGE=daedalus2718/bitcoind

encrypt_properties:
	openssl cast5-cbc -e -in ${SECURE_PROPERTIES} -out ${SECURE_PROPERTIES}.cast5 -pass env:BITCOIN_FLOW_PASSWORD

decrypt_properties:
	openssl cast5-cbc -d -in ${SECURE_PROPERTIES}.cast5 -out ${SECURE_PROPERTIES} -pass env:BITCOIN_FLOW_PASSWORD
	chmod 600 ${SECURE_PROPERTIES}
clean:
	rm -f ${SECURE_PROPERTIES}
	rm -f conf/current-*.conf
