# Como executar o projeto

É necessário ter JDK 21, com `JAVA_HOME` configurado, e Docker com Compose instalado e iniciado. No Windows, utilize o Docker Desktop com containers Linux, recomendo usar WSL. No Linux, instale também o Make.

**Opção Principal:** Utilize o codespaces, está configurado, use o make para subir o container e executar a aplicação.

Execute os comandos na raiz do projeto. A primeira execução precisa de acesso à internet para baixar as dependências.

## Windows (PowerShell)

Crie o arquivo de configuração com as variáveis de ambiente, conforme previsto em `.env.example`, deixe apenas com `.env`.

Carregue as configurações e inicie o banco junto a aplicação:

```powershell
docker compose up -d --wait
.\mvnw.cmd spring-boot:run
```

Caso esteja utilizando alguma IDE, lembre-se de iniciarlizar a aplicação com as variáveis de ambiente junto.

## Linux

Crie o arquivo de configuração com as variáveis de ambiente, caso ainda não exista, e inicie o projeto:

```bash
make run
```

O Makefile carrega o `.env`, inicia o banco e executa a aplicação.

## Acesso e encerramento

Acesse [http://localhost:8080](http://localhost:8080). O banco e os dados iniciais são preparados automaticamente na inicialização.

Para encerrar a aplicação, pressione **Ctrl+C** no terminal. Para parar o banco preservando os dados, execute em qualquer um dos sistemas:

```bash
docker compose stop
```
