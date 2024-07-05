
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
- **Maven**
- **PostgreSQL**

## Pré-requisitos

- **Java 17**: Certifique-se de ter o JDK 17 instalado.
- **Maven**: Certifique-se de ter o Maven instalado.
- **Clonar repositório do Script SQL da Bíblia**: Certifique-se de clonar e executar os scripts que estão neste [link](https://github.com/valdirsantos714/biblia-postgresql) em um banco de dados postgreSQL
- **Modificar o arquivo `application-dev.properties`**: Substituir  o campo `{URL_DO_SEU_BANCO_DE_DADOS}` pelo caminho do seu banco de dados e o campo `{SUA_SENHA}` pela senha do seu banco de dados 

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

1. Inicie a aplicação:

   ```bash
   mvn spring-boot:run
   ```

2. A API estará disponível em:

   ```
   http://localhost:8080
   ```

### Endpoints

- **Listar Livros**
    - **GET** `/livros/all`
    - **Resposta**:
      ```json
      [
        {
          "id": 18,
          "nome": "Salmos"
        },
        ...
      ]
      ```

- **Listar Capítulos**
    - **GET** `/livros/{idLivro}`
    - **Parâmetros**: `idLivro` - ID do livro
    - **Resposta**:
      ```json
      [
        1,2,3,4,5 ...
      ]
      ```

- **Listar Quantidade de Versículos**
    - **GET** `/livros/{livroId}/{idCapitulo}/verNumeroDeVersos`
    - **Parâmetros**: `livroId` - ID do livro, `idCapitulo` - ID do capítulo
    - **Resposta**:
      ```json
      [
        1,2,3,4,5 ...
      ]
      ```

- **Listar Versículos**
    - **GET** `/livros/{livroId}/{idCapitulo}`
    - **Parâmetros**: `livroId` - ID do livro, `idCapitulo` - ID do capítulo
    - **Resposta**:
      ```json
      [
        {

          "capitulo": 3,
          "versiculo": 16,
          "texto": "Porque Deus tanto amou o mundo que deu o seu Filho Unigênito, para que todo o que nele crer não pereça, mas tenha a vida eterna.",
          ...
        },
      ]
      ```


## Estrutura do Projeto

- `src/main/java/com/example/demo/`: Código fonte principal.
- `src/main/resources/`: Arquivos de configuração.
- `pom.xml`: Arquivo de configuração do Maven.

## Contribuição

1. Faça um fork do projeto.
2. Crie uma nova branch com a sua funcionalidade: `git checkout -b minha-funcionalidade`
3. Commit suas mudanças: `git commit -m 'Adicionar nova funcionalidade'`
4. Faça push para a branch: `git push origin minha-funcionalidade`
5. Abra um pull request.


## Contato

Se você tiver alguma dúvida ou sugestão, sinta-se à vontade para abrir uma issue ou entrar em contato.