package br.com.felipepinha.literAlura.repositories;

import br.com.felipepinha.literAlura.models.Idioma;
import br.com.felipepinha.literAlura.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT l FROM Livro l WHERE l.language = :idioma")
    List<Livro> findByIdioma(Idioma idioma);

}
