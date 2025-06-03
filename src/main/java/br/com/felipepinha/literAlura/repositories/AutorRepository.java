package br.com.felipepinha.literAlura.repositories;

import br.com.felipepinha.literAlura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
