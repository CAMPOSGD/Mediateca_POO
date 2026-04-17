package mediateca.modelos;

public class Libro extends MaterialEscrito {
    private String autor;
    private int numPaginas;
    private String isbn;
    private int anioPublicacion;
    private int unidadesDisponibles;

    public Libro(String codigoInterno, String titulo, String autor, int numPaginas, String editorial, String isbn, int anioPublicacion, int unidadesDisponibles) {
        super(codigoInterno, titulo, editorial);
        this.autor = autor;
        this.numPaginas = numPaginas;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }
}