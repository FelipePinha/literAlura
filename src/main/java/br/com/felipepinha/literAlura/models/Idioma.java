package br.com.felipepinha.literAlura.models;

public enum Idioma {
    EN, ES, FR, DE, PT, IT, OUTRO;

    public static Idioma fromString(String code) {
        if (code == null) return OUTRO;
        return switch (code.toLowerCase()) {
            case "en" -> EN;
            case "es" -> ES;
            case "fr" -> FR;
            case "de" -> DE;
            case "pt" -> PT;
            case "it" -> IT;
            default -> OUTRO;
        };
    }
}

