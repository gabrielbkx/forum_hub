# ForumHub API

API RESTful para um sistema de fórum com autenticação JWT, gerenciamento de usuários, cursos, tópicos e respostas.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Security (com JWT)
- Spring Data JPA / Hibernate
- Banco de dados relacional MySQL hospedado na AWS RDS
- Maven

## Funcionalidades

- Cadastro e autenticação de usuários com segurança JWT
- CRUD de Cursos
- CRUD de Tópicos vinculados a Cursos e Usuários
- CRUD de Respostas vinculadas a Tópicos e Usuários
- Controle de perfis/roles para usuários
- Validação básica de dados via Bean Validation (Jakarta Validation)


