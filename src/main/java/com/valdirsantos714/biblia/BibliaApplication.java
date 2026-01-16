package com.valdirsantos714.biblia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliaApplication {

	public static void main(String[] args) {
		System.out.println("════════════════════════════════════════════════════════");
		System.out.println("🚀 Iniciando API Bíblia...");
		System.out.println("════════════════════════════════════════════════════════");

		SpringApplication.run(BibliaApplication.class, args);

		System.out.println("════════════════════════════════════════════════════════");
		System.out.println("✅ API Bíblia iniciada com sucesso!");
		System.out.println("📖 Banco de dados carregado e pronto para uso");
		System.out.println("🌐 Acesse: http://localhost:8080");
		System.out.println("❤️ Endpoints disponíveis:");
		System.out.println("   - GET /livros/all - Listar todos os livros");
		System.out.println("   - GET /versiculo-do-dia - Obter versículo do dia");
		System.out.println("   - GET /health - Verificar status da API");
		System.out.println("════════════════════════════════════════════════════════");
	}

}
