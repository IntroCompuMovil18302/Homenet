package javeriana.edu.co.homenet.models;

public class OpinionGuia {

    private int calificacion;
    private String descripcion;
    private String guia;
    private String usuario;

    public OpinionGuia(int calificacion, String descripcion, String guia, String usuario) {
        this.calificacion = calificacion;
        this.descripcion = descripcion;
        this.guia = guia;
        this.usuario = usuario;
    }

    public OpinionGuia () {}

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGuia() {
        return guia;
    }

    public void setGuia(String guia) {
        this.guia = guia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
