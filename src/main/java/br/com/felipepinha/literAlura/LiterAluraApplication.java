package br.com.felipepinha.literAlura;

import br.com.felipepinha.literAlura.Principal.Principal;
import br.com.felipepinha.literAlura.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {
	@Autowired
	AutorRepository autorRepository;


	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(autorRepository);
		principal.exibeMenu();
	}
}
