# BillsCrud
O projeto é CRUD simples de contas a pagar, conforme a data de vencimento e data de pagamento é calculo uma mult em cima do valor original no momento da inserção.
É uma API feita com Java 11 e Spring boot, foi utilizado PostgreSQL como banco de dados.
- A regra de cálculo da multa é persistida no banco, através do Flyway, quando for rodada a API;
- Foi configurado um swagger para melhor visualização da API;
- Foi utilizado Junit e Mockito para fazer testes unitários e de integração;
- O tratamento da exceções está sendo feito através de do ControllerAdvice do Spring;
- Para rodar, é necessário clonar e pode ser executado através de alguma IDE (IntelliJ, Eclipse);
- É possível rodar através do docker, acessando a pasta raiz via terminal e rodando o comando "docker-compose up --build";
