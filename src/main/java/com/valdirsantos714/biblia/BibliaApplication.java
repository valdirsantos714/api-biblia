package com.valdirsantos714.biblia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BibliaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliaApplication.class, args);
		log.info("✅ Api Bíblia Iniciada com Sucesso!");
	}
}
