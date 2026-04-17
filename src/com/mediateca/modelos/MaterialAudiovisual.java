package mediateca.modelos;

public abstract class MaterialAudiovisual extends Material {
    private String duracion;
    private String genero;

    public MaterialAudiovisual(String codigoInterno, String titulo, String duracion, String genero) {
        super(codigoInterno, titulo);
        this.duracion = duracion;
        this.genero = genero;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}