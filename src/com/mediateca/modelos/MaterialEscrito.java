package mediateca.modelos;

public abstract class MaterialEscrito extends Material {
    private String editorial;

    public MaterialEscrito(String codigoInterno, String titulo, String editorial) {
        super(codigoInterno, titulo);
        this.editorial = editorial;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
}