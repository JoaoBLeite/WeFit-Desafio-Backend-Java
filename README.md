# WeFit | Desafio Backend Java

Este projeto foi desenvolvido como parte do processo seletivo da WeFit para a vaga de desenvolvedor backend Java.
Desenvolvido
por [João Pedro Barbosa](https://github.com/JoaoBLeite), esta aplicação é uma API RESTful para o cadastro de perfis de
pessoas jurídicas,
referidas no sistema como <em>"Legal Entities"</em>.

## Visão Geral do Projeto

O objetivo principal desta API é fornecer um sistema robusto e seguro para o registro das entidades, coletando
suas informações cadastrais, incluindo seu endereço. A aplicação foi construída utilizando as melhores práticas do
ecossistema Spring
Boot, com foco em:

1. Spring Boot: Para um desenvolvimento rápido e eficiente de aplicações.
2. Spring Data JPA: Para interação com o banco de dados, facilitando operações CRUD e mapeamento objeto-relacional.
3. H2 Database <em>(In-Memory)</em>: Utilizado para desenvolvimento e testes, proporcionando um ambiente de banco de
   dados leve e
   e prático.
4. Flyway: Para gerenciamento de migrações de banco de dados, garantindo o versionamento do banco.
5. Spring Security: Para autenticação e autorização, protegendo os endpoints da API.
6. JWT <em>(JSON Web Tokens)</em>: Implementado para autenticação <em>stateless</em> e segura.
7. Lombok: Para reduzir o <em>boilerplate code</em> em classes Java.
8. MapStruct: Para geração de <em>mappers</em> de forma performática e segura, convertendo DTOs em entidades e
   vice-versa.
9. Jakarta Bean Validation: Para validação declarativa de dados de entrada.
10. SLF4J: Para um sistema de <em>logging</em> configurável.
11. SpringDoc OpenAPI (Swagger UI): Para documentação interativa da API, facilitando o entendimento e o teste dos
    endpoints.

## Funcionalidades Principais

1. Cadastro de Pessoas Jurídicas <em>(Legal Entities)</em>: Endpoint dedicado para o registro de novas empresas,
   incluindo seus
   dados de CNPJ, responsável, contato e endereço.
2. Autenticação e Autorização: Sistema de login para obtenção de tokens JWT, permitindo acesso seguro aos recursos
   protegidos da API.
3. Gerenciamento de Usuários: Criação de um usuário administrador padrão na inicialização da aplicação para demonstração
   e testes.
4. Validação de Dados: Validação rigorosa dos dados de entrada, incluindo formatos específicos para CNPJ e CPF.

## Executando o projeto

### Variáveis de Ambiente:

A aplicação utiliza variáveis de ambiente para configurações sensíveis. Você pode defini-las diretamente no seu ambiente
ou no arquivo application.yml (apenas para testes):

1. ```DATABASE_USERNAME```: Usuário do banco de dados (e.g., root)
2. ```DATABASE_PASSWORD```: Senha do banco de dados (e.g., root)
3. ```SALT_LENGTH```: Comprimento do salt para o encoder de senha (e.g., 10)
4. ```ITERATIONS```: Iterações para o encoder de senha (e.g., 2048)
5. ```JWT_SECRET_KEY```: Chave secreta para assinatura JWT (e.g., uma string longa e aleatória)
6. ```JWT_EXPIRE_LENGTH```: Tempo de expiração do JWT em milissegundos (e.g., 3600000 para 1 hora)
7. ```ADM_USERNAME```: Email do usuário administrador padrão (e.g., john.doe@email.com)
8. ```ADM_PASSWORD```: Senha do usuário administrador padrão (e.g., adm123)

### Endpoints da API
A documentação interativa da API está disponível via Swagger UI após a execução da aplicação.

* Swagger UI: http://localhost:8080/api/swagger-ui/index.html
* H2 Console: http://localhost:8080/api/h2-console/ (Use ```jdbc:h2:mem:wefit-challenge-db``` como JDBC URL, e as credenciais
configuradas para ```DATABASE_USERNAME``` e ```DATABASE_PASSWORD``` para acessar).