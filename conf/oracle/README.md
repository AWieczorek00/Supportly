## Baza danych
Dla stworzenia baz danych na potrzebę testów E2E
	1. Na środowisku docker -v < 20.0
		1.1 Przekopoiwać do folder gdzie znaduje sie dockerfile scripts-init
		1.2 Uruchomić docker build -t sorbelix3_0_database-oracle -f Dockerfile-db-oracle .
		1.3 docker run -d -v ${PWD}/init-script:/opt/oracle/scripts/setup -p 1199:1521 --name sorbelix3_0_database-oracle sorbelix3_0_database-oracle
	2. Na środowisku docker -v > 20.0
		2.1 Przekopoiwać do folder gdzie znaduje sie dockerfile scripts-init
		2.2 Uruchomić docker compose