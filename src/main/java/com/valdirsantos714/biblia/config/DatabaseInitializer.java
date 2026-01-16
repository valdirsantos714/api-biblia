package com.valdirsantos714.biblia.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class DatabaseInitializer {

    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());
    private static final int BATCH_SIZE = 50;

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public CommandLineRunner initializeDatabase(DataSource dataSource) {
        return args -> {
            try {
                //waitForDatabase(dataSource);

                File sqlFile = new File("biblia.sql");

                if (!sqlFile.exists()) {
                    logger.warning("⚠️ Arquivo biblia.sql não encontrado na raiz do projeto");
                    return;
                }

                try (Connection connection = dataSource.getConnection()) {
                    logger.info("✅ Conexão com o banco de dados estabelecida");


                    logger.info("🚀 Verificando se é necessário carregar arquivo biblia.sql...");
                    if (isDatabaseAlreadyLoaded(connection)) {
                        logger.info("✓ Banco de dados já contém os dados necessários. Pulando carregamento do biblia.sql");
                        return;
                    }

                    logger.info("📝 Lendo arquivo biblia.sql...");
                    String sqlScript = new String(Files.readAllBytes(sqlFile.toPath()));

                    logger.info("⚙️ Processando SQL...");
                    List<String> statements = parseSQLStatements(sqlScript);
                    logger.info("📊 Total de statements encontrados: " + statements.size());

                    logger.info("📝 Executando script SQL (este processo pode levar alguns minutos)...");
                    executeSQLStatements(connection, statements);

                    logger.info("✓ Script biblia.sql executado com sucesso!");
                }
            } catch (InterruptedException e) {
                logger.warning("⚠️ Erro ao aguardar inicialização do banco de dados: " + e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.severe("❌ Erro ao executar script biblia.sql: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private List<String> parseSQLStatements(String sqlScript) {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();

        String[] lines = sqlScript.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                continue;
            }

            currentStatement.append(" ").append(trimmedLine);

            if (trimmedLine.endsWith(";")) {
                String statement = currentStatement.toString().trim();
                if (statement.endsWith(";")) {
                    statement = statement.substring(0, statement.length() - 1).trim();
                }

                if (!statement.isEmpty()) {
                    statements.add(statement);
                }

                currentStatement = new StringBuilder();
            }
        }

        return statements;
    }

    private void executeSQLStatements(Connection connection, List<String> statements) throws Exception {
        connection.setAutoCommit(false);

        try (Statement statement = connection.createStatement()) {
            int count = 0;

            for (String stmt : statements) {
                if (!stmt.isEmpty()) {
                    try {
                        statement.addBatch(stmt);
                        count++;

                        if (count % BATCH_SIZE == 0) {
                            statement.executeBatch();
                            connection.commit();
                            logger.info("⚙️ " + count + " statements executados e commitados...");
                        }
                    } catch (Exception e) {
                        logger.warning("⚠️ Erro ao adicionar ao batch: " + e.getMessage());
                    }
                }
            }

            // Executa o batch restante
            if (count % BATCH_SIZE != 0) {
                statement.executeBatch();
                connection.commit();
            }

            logger.info("✓ Total de " + count + " statements executados com sucesso");
        } catch (Exception e) {
            connection.rollback();
            logger.severe("❌ Erro durante execução do batch: " + e.getMessage());
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void waitForDatabase(DataSource dataSource) throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            try (Connection c = dataSource.getConnection()) {
                return;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        throw new IllegalStateException("Banco não disponível");
    }

    private boolean isDatabaseAlreadyLoaded(Connection connection) {
        String sql = "SELECT 1 FROM livros LIMIT 1";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            boolean hasData = resultSet.next();
            logger.info("ℹ️ Verificação da tabela 'livros': " +
                    (hasData ? "dados encontrados" : "tabela vazia"));

            return hasData;

        } catch (Exception e) {
            logger.info("ℹ️ Tabela 'livros' ainda não existe ou não está acessível");
            return false;
        }
    }
}

