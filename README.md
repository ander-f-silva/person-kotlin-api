# Projeto Cadastro de Pessoas

Desenvolver uma solução para realizar a manutenção de dados do cliente. Neste serviço esta contemplado as seguintes funcionalidades:

- Registrar uma pessoas;
- Registrar um lote de pesssoas;
- Listar as pessoas registras;
- Remover logicamente uma pessoa;
- Atualizar uma pessoa;
- Consultar uma pessoa informando nome, sobrenome e/ou cpf

## Reflexão sobre o problema

Para elaborar um mvp (projeto inicial) pensei em dois frameworks.
Primeiro o Vert.x, pois este framework um container leve e trabalha com modelo de programação reativa.
Segundo foi Spring 2 MVC que possui muitos projetos que são faceis de integrar com diversos tipos de banco de dadoe e segue padrão de cofigurações muito fácil de utlizar.

Acabei adotando o Spring, pois este oferece recursos de gestão configuração fácil e rápido para construir um microserviço.
Para não haver depedência com infra local estou virtualizando utilizando container docker.

Os banco de dados h2 para ambiente locais e Heroku, o Mysql com docker compose. É importante destacar que usei o modelo relacional por que preferi trabalhar conceito ACID (focado na consistência de dados e garantia que dados não estarão diventes quando as opeções de crud executarem simultaneamente).

Com Spring Boot é possivel realizar um setup de projeto simples e facill para montar o ambiente.

Quero destacar o uso da linguagem Kotlin (a linguagem que tenho mais performance é Java), mas visando uma solução com código simples e que seja menos verbosa. Acredito que esta linguagem seria melhor do que usar Java com Lombok (elimina depdência com IDEA e Biblioteca).

Por entender que o mundo de desenvolvimento esta globalizado, utilizei como idioma o Inglês para escrever o código fonte e as apis com payload.

## Tecnologias utilizadas

* Linguagem Java - Versão 1.8 (Oracle 1.8.0_121)

```
java version "1.8.0_121"
Java(TM) SE Runtime Environment (build 1.8.0_121-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.121-b13, mixed mode)
```

* Gradle 4 - Ferramenta de Build

```

------------------------------------------------------------
Gradle 4.10.2
------------------------------------------------------------

Build time:   2018-09-19 18:10:15 UTC
Revision:     b4d8d5d170bb4ba516e88d7fe5647e2323d791dd

Kotlin DSL:   1.0-rc-6
Kotlin:       1.2.61
Groovy:       2.4.15
Ant:          Apache Ant(TM) version 1.9.11 compiled on March 23 2018
JVM:          1.8.0_191 (Oracle Corporation 25.191-b12)
OS:           Linux 4.18.0-13-generic amd64

```


* Versionador - Git

* Banco de Dados - H2 e Mysql

* Spring Web (MVC) - Framerwork Web para geração das API's (versão 2.0.4) com Tomcat 8

* Spring Boot - Setup de projeto

O repositório utilizado é o Bitbuket, onde utilizei o Git flow com a branch develop e master para gerenciar o fonte.

Para realizar o CI usei o Pipilene do Bitbuket com a plataforma Pass Heroku para relizar o deploy da API.

Para os teste integrados utilizei a lib Aspen, RestAssure e FixtureFactory.


# Documentação através do swagger (versão 2.8)

O projeto possui um documentação de API através do swagger (Neste possui como utilizar os endpoints da api).

Para acessar:

### Api: person-resource : Person Resource

http://localhost:9080/api/swagger-ui.html ou http://api-person.herokuapp.com/api/swagger-ui.html

![Swagger](/images/swagger.png)

## Payloads 

Para representar as entradas dos serviços de PUT  e POST (para serviços que opera para uma pessoa).

Segue:

```
{
  "address": {
    "city": "string",
    "complement": "string",
    "neighborhood": "string",
    "number": "string",
    "publicPlace": "string",
    "state": "string"
  },
  "birthDate": "string",
  "document": "string",
  "emails": [
    {
      "address": "string"
    }
  ],
  "lastName": "string",
  "name": "string",
  "phones": [
    {
      "areaCode": 0,
      "number": 0,
      "type": "string"
    }
  ]
}	
```

O serviço de POST (lote) ou GET para retorno das consultas.

Segue 

```
{
  "persons": [
    {
      "address": {
        "city": "string",
        "complement": "string",
        "neighborhood": "string",
        "number": "string",
        "publicPlace": "string",
        "state": "string"
      },
      "birthDate": "string",
      "document": "string",
      "emails": [
        {
          "address": "string"
        }
      ],
      "lastName": "string",
      "name": "string",
      "phones": [
        {
          "areaCode": 0,
          "number": 0,
          "type": "string"
        }
      ]
    }
  ]
}
```

Observação: O campo CPF esta sendo representado com document.


## Para exeuctar o build, testes e realizar o start do programa

Para executar o versão no ambiente local é necessario ter versão  do java 8.

Não precisa instalar o gradle, pois na raiz do projeto possui o gradle wrapper que pode ser usado no Windows,Linux e Mac.


Faça o clone no projeto na pasta desejada e depois na pasta do projeto:

```
cd person
./person 
```


Execute na raiz do comando para rodar os testes:

```
./gradlew test
```

Para executar o build:

```
./gradlew build
```

Observação: Utilizei o jacoco com 50% de cobertura para começar a melhorar a cobertura gradativamente

Segue o caminho: 

```
./persons/build/jacocoHtml/index.html
```

Para fazer o deploy podemos proceder:

Acesse a pasta build/libs e execute o comando para rodar o programa:

```
java -jar persons-0.0.1.jar
```

Ou usando o comando :

```
./gradlew bootRun
```

Outra solução foi utilizar docker para não depender do SO da máquina.

```
./gradlew build
 docker-compose up
```

Para verificar se a aplicação esta no ar e "saudavel", utilize a chamada do actuator.

```
http://localhost:9080/api/actuator/health
```

Ou caso esteja no heroku

```
http://api-person.herokuapp.com/api/actuator/health
``

## Gestão do Projeto e técnicas para construção da API

Não precisei usar Kaban paraa administrar as atividades, mas sempre me foquei na documentação passada.

Mas as etapas foram:

*  Criação do projeto no https://start.spring.io/;
*  Construção dos scripts para criação das tabelas;
*  Contrução das classes de dominio;
*  Construção dos testes integrados;
*  Construção da API e mecanismo para armazenar os dados;
*  Inclui os serviço na plataforma Heroku.

