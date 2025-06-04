package br.com.felipepinha.literAlura.Principal;

import br.com.felipepinha.literAlura.models.*;
import br.com.felipepinha.literAlura.repositories.AutorRepository;
import br.com.felipepinha.literAlura.repositories.LivroRepository;
import br.com.felipepinha.literAlura.service.ConsumoApi;
import br.com.felipepinha.literAlura.service.ConverteDados;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner scanner = new Scanner(System.in);
    private AutorRepository autorRepository;
    private LivroRepository livroRepository;

    String url = "https://gutendex.com/books/?search=";
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();

    public Principal(AutorRepository autorRepository, LivroRepository livroRepository) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }

    public void exibeMenu() {
        int opt = -1;

        while(opt != 0) {
            System.out.println("""
                    =========================
                    Escolha o número de sua opção
                    
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros por idioma
                    
                    
                    0 - Sair
                    """);
            opt = scanner.nextInt();
            scanner.nextLine();

            switch (opt) {
                case 1:
                    buscaLivro();
                    break;
                case 2:
                    livrosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresVivosNoAno();
                    break;
                case 5:
                    livroPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscaLivro() {
        System.out.println("Digite o título do livro");
        var titulo = scanner.nextLine();

        String busca = url + titulo.replace(" ", "%20");
        String json = consumoApi.obterDados(busca);
        GutendexResponse resposta = conversor.obterDados(json, GutendexResponse.class);

        if (resposta.results().isEmpty()) {
            System.out.println("Não foram encontrados resultados para " + titulo);
            System.out.println("Tente novamente...");
            return;
        }

        LivroDTO livroDto = resposta.results().get(0);

        for (AutorDTO autorDto : livroDto.authors()) {
            // Verifica se o autor já existe no banco
            Optional<Autor> autorOptional = autorRepository.findByNameAndBirthYearAndDeathYear(
                    autorDto.name(), autorDto.birth_year(), autorDto.death_year());

            Autor autor = autorOptional.orElseGet(() -> {
                Autor novoAutor = new Autor();
                novoAutor.setName(autorDto.name());
                novoAutor.setBirthYear(autorDto.birth_year());
                novoAutor.setDeathYear(autorDto.death_year());
                return novoAutor;
            });

            Livro livro = new Livro();
            livro.setTitle(livroDto.title());
            livro.setDownloadCount(livroDto.download_count());
            livro.setLanguage(Idioma.fromString(
                    livroDto.languages().isEmpty() ? null : livroDto.languages().get(0)
            ));
            livro.setAutor(autor);

            if (autor.getId() == null) {
                autor.setLivros(List.of(livro));
                autorRepository.save(autor);
            } else {
                livroRepository.save(livro);
            }

            exibeLivro(livro);
        }
    }


    private void livrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();

        livros.forEach(l -> exibeLivro(l));
    }

    private  void autoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        autores.forEach(a -> exibeAutor(a));
    }

    private void autoresVivosNoAno() {
        System.out.println("Informe um ano");
        var ano = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autores = autorRepository.findAutoresVivosNoAno(ano);

        autores.forEach(a -> exibeAutor(a));
    }

    private void livroPorIdioma() {
        System.out.println("Digite a sigla de um idioma (ex: EN, PT, FR)");
        var idiomaBuscado = scanner.nextLine().trim().toUpperCase();

        Idioma idioma = Idioma.valueOf(idiomaBuscado);

        List<Livro> livros = livroRepository.findByIdioma(idioma);

        livros.forEach(l -> exibeLivro(l));
    }

    private void exibeLivro(Livro livro) {
        System.out.println("------- LIVRO -------");
        System.out.println("""
                Titulo: %s
                Autor: %s
                Idioma: %s
                Número de downloads: %s
                """.formatted(livro.getTitle(), livro.getAutor().getName(),
                        livro.getLanguage(), livro.getDownloadCount())
                );
    }

    private void exibeAutor(Autor autor) {
        String titulos = autor.getLivros().stream()
                        .map(Livro::getTitle)
                        .collect(Collectors.joining(", ", "[", "]"));

        System.out.println("------- Autor -------");
        System.out.println("""
                Nome: %s
                Ano de nascimento: %s
                Ano de falescimento: %s
                livros: %s
                """.formatted(autor.getName(), autor.getBirthYear(),
                autor.getDeathYear(), titulos)
        );
    }
}
