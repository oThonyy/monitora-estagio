.DEFAULT_GOAL := help

COMPOSE ?= docker compose

-include .env

MYSQL_PORT ?= 3306
MYSQL_DATABASE ?= fatec_estagios
MYSQL_USER ?= fatec
MYSQL_PASSWORD ?= fatec123
DB_URL ?= jdbc:mysql://localhost:$(MYSQL_PORT)/$(MYSQL_DATABASE)?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
DB_USERNAME ?= $(MYSQL_USER)
DB_PASSWORD ?= $(MYSQL_PASSWORD)

export MYSQL_PORT MYSQL_DATABASE MYSQL_USER MYSQL_PASSWORD MYSQL_ROOT_PASSWORD
export DB_URL DB_USERNAME DB_PASSWORD

.PHONY: help up stop down status logs run test build clean

db-up:
	$(COMPOSE) up -d --wait

db-stop:
	$(COMPOSE) stop

db-down:
	$(COMPOSE) down

db-exec:
	$(COMPOSE) exec mysql sh -lc 'mysql -u"$$MYSQL_USER" -p"$$MYSQL_PASSWORD" "$$MYSQL_DATABASE"'

db-status:
	$(COMPOSE) ps

db-logs:
	$(COMPOSE) logs -f mysql

run: up
	bash mvnw spring-boot:run

test:
	bash mvnw test

build:
	bash mvnw package

clean:
	bash mvnw clean


help:
	@printf '%s\n' \
	  'make db-up      - Inicia o MySQL' \
	  'make db-stop    - Para o banco preservando o container e os dados' \
	  'make db-down    - Remove o container e a rede, preservando os dados' \
	  'make db-exec    - 'Executa o container com o banco de dados' \
	  'make db-status  - Exibe o estado dos containers' \
	  'make db-logs    - Acompanha os logs do banco' \
	  'make run        - Inicia o banco e executa a aplicação' \
	  'make test       - Executa os testes' \
	  'make build      - Executa os testes e gera o JAR' \
	  'make clean      - Limpa os arquivos de build' \