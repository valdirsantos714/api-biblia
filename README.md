
# API Bíblia

Uma simples API desenvolvida com o propósito de fornecer acesso aos livros, capítulos e versículos da bíblia. 
## Funcionalidades

- **Listar Livros**: Obtenha a lista completa de livros da bíblia.
- **Listar Capítulos**: Obtenha a lista de capítulos de um livro específico.
- **Listar Quantidade de Versículos**: Obtenha a lista contendo a quantidade de versículos de um capítulo específico.
- **Listar Versículos**: Obtenha uma lista contendo os textos de um capítulo de um livro específico.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Validation**
- **Spring RestClient**
- **Spring Actuator**
- **Swagger (SpringDoc OpenAPI)**
- **Resilience4j** (Circuit Breaker)
- **PostgreSQL**
- **Lombok**
- **Maven**

## Pré-requisitos

- **Java 17**: Certifique-se de ter o JDK 17 instalado.
- **Maven**: Certifique-se de ter o Maven instalado (ou use o mvnw fornecido).
- **PostgreSQL**: Banco de dados PostgreSQL em execução.
- **Docker** (opcional): Para subir o banco de dados usando o comando ```docker compose up -d ``` .
- **Configuração de variáveis de ambiente**: Atualize as credenciais de banco de dados no arquivo `application.yaml`. 

## Instalação

1. Clone o repositório:

   ```bash
   git clone https://github.com/valdirsantos714/api-biblia.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd api-biblia
   ```

3. Compile o projeto e instale as dependências:

   ```bash
   mvn clean install
   ```

## Uso

1. Configure o banco de dados PostgreSQL e atualize as credenciais no arquivo `src/main/resources/application.yaml` ou suba o banco de dados usando o comando ```docker compose up -d```.


2. Inicie a aplicação:

   ```bash
   mvn spring-boot:run
   ```

2. A API estará disponível em:

   ```
   http://localhost:8080
   ```
   
### Versões da Bíblia incluídas
 - Nova Versão Transformadora (NVT)
 - Almeida Revista e Atualizada (ARA)
 - Nova Versão Internacional (NVI)
 - Nova Tradução na Linguagem de Hoje (NTLH)

### Endpoints

#### Livros - `/livros`

- **Listar todos os livros**
    - **GET** `/livros/all`
    - **Descrição**: Retorna uma lista completa de todos os livros bíblicos disponíveis
    - **Resposta (200)**:
      ```json
      [
        {
          "id": 0,
          "nome": "Gênesis",
          "abreviacao": "gn",
          "testamento": {
            "id": 1,
            "nome": "Velho Testamento"
          }
        },
        {
          "id": 1,
          "nome": "Êxodo",
          "abreviacao": "ex",
          "testamento": {
            "id": 1,
            "nome": "Velho Testamento"
          }
        }
      ]
      ```

- **Buscar versos por ID do livro e capítulo**
    - **GET** `/livros/{id_livro}/{capitulo}`
    - **Parâmetros**: 
      - `id_livro` - ID único do livro bíblico (ex: 1)
      - `capitulo` - Número do capítulo (ex: 1)
    - **Descrição**: Retorna todos os versos de um capítulo específico na versão padrão (NVT)
    - **Resposta (200)**:
      ```json
      [
        {
          "id": 1,
          "versao": {
            "id": 2,
            "nome": "nvt"
          },
          "livro": {
            "id": 0,
            "nome": "Gênesis",
            "abreviacao": "gn",
            "testamento": {
              "id": 1,
              "nome": "Velho Testamento"
            }
          },
          "capitulo": 1,
          "versiculo": 1,
          "texto": "No princípio, criou Deus os céus e a terra.",
          "testamento": 1,
      ...
        } 
      ]
      ```

- **Buscar versos por nome do livro e capítulo**
    - **GET** `/livros/buscarPorNome/{nomeLivro}/{capitulo}`
    - **Parâmetros**: 
      - `nomeLivro` - Nome completo ou abreviação do livro (ex: Gênesis)
      - `capitulo` - Número do capítulo (ex: 1)
    - **Descrição**: Retorna todos os versos de um capítulo específico identificando o livro pelo nome, na versão padrão (NVT)
    - **Resposta (200)**:
      ```json
      [
        {
          "id": 1,
          "versao": {
            "id": 2,
            "nome": "nvt"
          },
          "livro": {
            "id": 0,
            "nome": "Gênesis",
            "abreviacao": "gn",
            "testamento": {
              "id": 1,
              "nome": "Velho Testamento"
            }
          },
          "capitulo": 1,
          "versiculo": 1,
          "texto": "No princípio, criou Deus os céus e a terra.",
          "testamento": 1,
      ...
        }
      ]
      ```

- **Buscar números de versos por ID do livro e capítulo**
    - **GET** `/livros/{livro}/{capitulo}/verNumeroDeVersos`
    - **Parâmetros**: 
      - `livro` - ID único do livro bíblico (ex: 1)
      - `capitulo` - Número do capítulo (ex: 1)
    - **Descrição**: Retorna uma lista com os números dos versos existentes em um capítulo específico
    - **Resposta (200)**:
      ```json
      [1, 2, 3, 4, 5, ..., 31]
      ```

- **Buscar versos por versão bíblica**
    - **GET** `/livros/{nomeVersao}/{id_livro}/{capitulo}`
    - **Parâmetros**: 
      - `nomeVersao` - Nome da versão bíblica (ex: acf, kjv, nvt)
      - `id_livro` - ID único do livro bíblico (ex: 1)
      - `capitulo` - Número do capítulo (ex: 1)
    - **Descrição**: Retorna versos de um capítulo específico em uma versão bíblica escolhida
    - **Resposta (200)**:
      ```json
      [
        {
          "id": 1,
          "versao": {
            "id": 2,
            "nome": "nvt"
          },
          "livro": {
            "id": 0,
            "nome": "Gênesis",
            "abreviacao": "gn",
            "testamento": {
              "id": 1,
              "nome": "Velho Testamento"
            }
          },
          "capitulo": 1,
          "versiculo": 1,
          "texto": "No princípio, criou Deus os céus e a terra.",
          "testamento": 1,
      ...
        }
      ]
      ```

- **Busca detalhada por nome de livro e versão**
    - **GET** `/livros/buscaDetalhada/{nomeVersao}/{livro}/{capitulo}`
    - **Parâmetros**: 
      - `nomeVersao` - Nome da versão bíblica (ex: acf, kjv, nvt)
      - `livro` - Nome completo ou abreviação do livro (ex: Gênesis)
      - `capitulo` - Número do capítulo (ex: 1)
    - **Descrição**: Retorna versos de um capítulo específico buscando o livro pelo nome e escolhendo uma versão bíblica
    - **Resposta (200)**:
      ```json
      [
        {
          "id": 1,
          "versao": {
            "id": 2,
            "nome": "nvt"
          },
          "livro": {
            "id": 0,
            "nome": "Gênesis",
            "abreviacao": "gn",
            "testamento": {
              "id": 1,
              "nome": "Velho Testamento"
            }
          },
          "capitulo": 1,
          "versiculo": 1,
          "texto": "No princípio, criou Deus os céus e a terra.",
          "testamento": 1,
      ...
        }
      ]
      ```

#### Versículos do Dia - `/versiculos`

- **Listar todos os versículos do dia**
    - **GET** `/versiculos/all`
    - **Descrição**: Retorna uma lista completa de todos os versículos do dia cadastrados no sistema
    - **Resposta (200)**:
      ```json
      [
        {
          "id": 1,
          "verso": {
            "id": 1,
            "capitulo": 3,
            "versiculo": 16,
            "texto": "Porque Deus tanto amou o mundo que deu o seu Filho Unigênito, para que todo o que nele crer não pereça, mas tenha a vida eterna.",
            "testamento": 2,
            "versao": {
              "id": 1,
              "nome": "NVT"
            },
            "livro": {
              "id": 43,
              "nome": "João",
              "abreviacao": "Jo",
              "testamento": {
                "id": 2,
                "nome": "Novo Testamento"
              }
            }
          }
        }
      ]
      ```

- **Buscar versículo do dia por ID**
    - **GET** `/versiculos/{id}`
    - **Parâmetros**: 
      - `id` - ID único do versículo do dia (ex: 1)
    - **Descrição**: Retorna um versículo específico do dia identificado por seu ID
    - **Resposta (200)**:
      ```json
      {
        "id": 1,
        "verso": {
          "id": 1,
          "capitulo": 3,
          "versiculo": 16,
          "texto": "Porque Deus tanto amou o mundo que deu o seu Filho Unigênito, para que todo o que nele crer não pereça, mas tenha a vida eterna.",
          "testamento": 2,
          "versao": {
            "id": 1,
            "nome": "NVT"
          },
          "livro": {
            "id": 43,
            "nome": "João",
            "abreviacao": "Jo",
            "testamento": {
              "id": 2,
              "nome": "Novo Testamento"
            }
          }
        }
      }
      ```

- **Criar novo versículo do dia**
    - **POST** `/versiculos`
    - **Descrição**: Cria um novo versículo do dia e o persiste no banco de dados
    - **Corpo da Requisição**:
      ```json
      {
        "verso": {
          "id": 1
        }
      }
      ```
    - **Resposta (201)**:
      ```json
      {
        "id": 1,
        "verso": {
          "id": 1,
          "capitulo": 3,
          "versiculo": 16,
          "texto": "Porque Deus tanto amou o mundo que deu o seu Filho Unigênito, para que todo o que nele crer não pereça, mas tenha a vida eterna.",
          "testamento": 2,
          "versao": {
            "id": 1,
            "nome": "NVT"
          },
          "livro": {
            "id": 43,
            "nome": "João",
            "abreviacao": "Jo",
            "testamento": {
              "id": 2,
              "nome": "Novo Testamento"
            }
          }
        }
      }
      ```

## Documentação da API

A documentação interativa da API está disponível via Swagger/OpenAPI:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs


## Estrutura do Projeto

- `src/main/java/com/valdirsantos714/biblia/`: Código fonte principal.
- `src/main/resources/`: Arquivos de configuração.
- `src/test/`: Testes automatizados.
- `pom.xml`: Arquivo de configuração do Maven.
- `biblia.sql`: Script SQL para popular o banco de dados com os dados da bíblia nas versões mencionadas acima.
- `docker-compose.yml`: Arquivo para subir o banco de dados PostgreSQL via Docker.
- `Dockerfile`: Arquivo para criar a imagem Docker da aplicação.
- `src/main/resources/application.yaml`: Arquivo de configuração da aplicação Spring Boot.

## Contribuição

1. Faça um fork do projeto.
2. Crie uma nova branch com a sua funcionalidade: `git checkout -b minha-funcionalidade`
3. Commit suas mudanças: `git commit -m 'Adicionar nova funcionalidade'`
4. Faça push para a branch: `git push origin minha-funcionalidade`
5. Abra um pull request.


## Contato

Se você tiver alguma dúvida ou sugestão, sinta-se à vontade para abrir uma issue ou entrar em contato.