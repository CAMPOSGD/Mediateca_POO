package mediateca.modelos;

public class DVD extends MaterialAudiovisual {
    private String director;
    private int unidadesDisponibles;

    public DVD(String codigoInterno, String titulo, String director, String duracion, String genero, int unidadesDisponibles) {
        super(codigoInterno, titulo, duracion, genero);
        this.director = director;
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public int getUnidadesDisponibles() { return unidadesDisponibles; }
    public void setUnidadesDisponibles(int unidadesDisponibles) { this.unidadesDisponibles = unidadesDisponibles; }
}