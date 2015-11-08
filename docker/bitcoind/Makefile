# bitcoind Makefile
# 
# derived from http://ejohn.org/blog/keeping-passwords-in-source-control/
# 
# this script requires that the BITCOIN_FLOW_PASSWORD environment variable be set

.PHONY: decrypt_properties encrypt_properties build

RPC_PROPERTIES=conf/rpc.properties
BITCOIND_IMAGE=daedalus2718/bitcoind

encrypt_properties:
	openssl cast5-cbc -e -in ${RPC_PROPERTIES} -out ${RPC_PROPERTIES}.cast5 -pass env:BITCOIN_FLOW_PASSWORD

decrypt_properties:
	openssl cast5-cbc -d -in ${RPC_PROPERTIES}.cast5 -out ${RPC_PROPERTIES} -pass env:BITCOIN_FLOW_PASSWORD
	chmod 600 ${RPC_PROPERTIES}

build_image:
	docker build -t ${BITCOIND_IMAGE} .

publish_image:
	echo "if this doesn't work you may need to first log in via docker login"
	docker push ${BITCOIND_IMAGE}
