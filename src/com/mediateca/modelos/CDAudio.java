package mediateca.modelos;

public class CDAudio extends MaterialAudiovisual {
    private String artista;
    private int numCanciones;
    private int unidadesDisponibles;

    public CDAudio(String codigoInterno, String titulo, String artista, String genero, String duracion, int numCanciones, int unidadesDisponibles) {
        super(codigoInterno, titulo, duracion, genero);
        this.artista = artista;
        this.numCanciones = numCanciones;
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getNumCanciones() {
        return numCanciones;
    }

    public void setNumCanciones(int numCanciones) {
        this.numCanciones = numCanciones;
    }

    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }
}