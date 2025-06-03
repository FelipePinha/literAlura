package br.com.felipepinha.literAlura.repositories;

import br.com.felipepinha.literAlura.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
