package br.com.felipepinha.literAlura.models;

import br.com.felipepinha.literAlura.models.AutorDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LivroDTO(String title, List<AutorDTO> authors, List<String> languages, int download_count) {

}
