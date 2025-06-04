package br.com.felipepinha.literAlura.repositories;

import br.com.felipepinha.literAlura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.birthYear <= :ano AND a.deathYear > :ano")
    List<Autor> findAutoresVivosNoAno(Integer ano);

    Optional<Autor> findByNameAndBirthYearAndDeathYear(String name, Integer birthYear, Integer deathYear);

}
