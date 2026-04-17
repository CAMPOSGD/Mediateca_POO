package mediateca.modelos;

public abstract class Material {
    private String codigoInterno;
    private String titulo;

    public Material(String codigoInterno, String titulo) {
        this.codigoInterno = codigoInterno;
        this.titulo = titulo;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}