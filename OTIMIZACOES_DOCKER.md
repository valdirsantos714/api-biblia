# Otimizações Docker Compose para SQLs Pesados e Grandes

## PostgreSQL 16 - Otimizações

### Configurações de Buffer e Cache
- **shared_buffers=512MB**: Aumentado para cache de dados frequentemente acessados
- **effective_cache_size=2GB**: Deixa o planner de queries saber quanto cache está disponível
- **maintenance_work_mem=256MB**: Para operações de índice e vacuum mais rápidas

### Configurações de Escrita (WAL)
- **wal_buffers=16MB**: Buffer maior para writes mais eficientes
- **min_wal_size=1GB**: Mantém mais logs para melhor performance de checkpoint
- **max_wal_size=4GB**: Permite checkpoints menos frequentes

### Configurações de Memória de Trabalho
- **work_mem=16MB**: Memória por operação de classificação/hash
- **maintenance_work_mem=256MB**: Para operações de manutenção

### Otimizações do Planner
- **effective_io_concurrency=200**: Otimiza para I/O paralelo
- **random_page_cost=1.1**: Reduz custos estimados de leitura aleatória (melhor para SSDs)
- **default_statistics_target=100**: Análise mais detalhada das tabelas

### Limites do Sistema
- **net.core.somaxconn=65535**: Fila de conexões TCP aumentada
- **net.ipv4.ip_local_port_range=1024 65535**: Range maior de portas

---

## Spring Boot API - Otimizações

### Connection Pool (HikariCP)
```
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE: "20"
```
- Máximo de 20 conexões simultâneas para lidar com queries pesadas

```
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE: "5"
```
- Mantém 5 conexões sempre prontas

```
SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT: "30000"
SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT: "600000"
SPRING_DATASOURCE_HIKARI_MAX_LIFETIME: "1800000"
```
- Timeouts aumentados para queries longas

### Otimizações Hibernate
```
SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_BATCH_SIZE: "50"
```
- Agrupa até 50 inserts/updates em um único batch

```
SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_FETCH_SIZE: "50"
```
- Busca 50 linhas por vez para melhor performance em queries grandes

```
SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_STATEMENT_TIMEOUT: "600"
```
- Timeout de 10 minutos para queries muito pesadas

### Configurações de JVM
```
JAVA_OPTS: "-Xms512m -Xmx1536m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```
- **-Xms512m -Xmx1536m**: 512MB inicial, até 1.5GB de heap
- **-XX:+UseG1GC**: Garbage collector otimizado para aplicações com grandes heaps
- **-XX:MaxGCPauseMillis=200**: Pausa de GC limitada a 200ms

### Limites de Recursos
```
deploy.resources.limits:
  cpus: '2'
  memory: 2G
deploy.resources.reservations:
  cpus: '1'
  memory: 1G
```
- Limite máximo de 2 CPUs e 2GB de RAM
- Reserva garantida de 1 CPU e 1GB de RAM

---

## Quando Usar Essas Configurações

✅ **Use se você tem:**
- SQLs complexos com JOINs múltiplos
- Queries que retornam grandes volumes de dados
- Muitas operações de aggregação (COUNT, SUM, etc)
- Relatórios pesados
- Processamento batch em paralelo

⚠️ **Ajuste se você notar:**
- Uso de memória muito alto: Reduza `shared_buffers`, `effective_cache_size`
- Queries ainda lentas: Aumente `MAXIMUM_POOL_SIZE`
- Timeout em queries: Aumente `SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_STATEMENT_TIMEOUT`
- High I/O: Aumente `work_mem` e `maintenance_work_mem`

---

## Monitoramento

Para monitorar a performance:

```bash
# Entrar no container PostgreSQL
docker exec -it biblia_postgres psql -U biblia_user -d biblia_db

# Queries lentas (dentro do psql)
SELECT query, calls, mean_time 
FROM pg_stat_statements 
ORDER BY mean_time DESC LIMIT 10;

# Ver conexões ativas
SELECT datname, usename, application_name, state 
FROM pg_stat_activity 
WHERE datname = 'biblia_db';

# Ver índices e suas estatísticas
SELECT schemaname, tablename, indexname, idx_scan 
FROM pg_stat_user_indexes 
ORDER BY idx_scan DESC;
```

---

## Dicas de Performance Adicionais

1. **Índices**: Certifique-se de ter índices nas colunas usadas em WHERE clauses e JOINs
2. **EXPLAIN ANALYZE**: Use antes de colocar em produção para verificar planos de execução
3. **Particionamento**: Para tabelas muito grandes, considere particionar por data ou range
4. **Conexão Pooling**: Nunca crie uma nova conexão por request, o HikariCP já faz isso
5. **Lazy Loading**: Cuidado com N+1 queries, use eager loading quando apropriado

