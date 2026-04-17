package mediateca.modelos;

public class Revista extends MaterialEscrito {
    private String periodicidad;
    private String fechaPublicacion;
    private int unidadesDisponibles;

    public Revista(String codigoInterno, String titulo, String editorial, String periodicidad, String fechaPublicacion, int unidadesDisponibles) {
        super(codigoInterno, titulo, editorial);
        this.periodicidad = periodicidad;
        this.fechaPublicacion = fechaPublicacion;
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }
}