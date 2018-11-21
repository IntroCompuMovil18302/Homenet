package javeriana.edu.co.homenet.models;

public class OpinionAlojamiento {
    String id;
    String alojamiento;
    double calificacion;
    String comentario;
    String usuario;

    public OpinionAlojamiento(){

    }

    public OpinionAlojamiento(String id,String alojamiento, double calificacion, String comentario, String usuario) {
        this.id = id;
        this.alojamiento = alojamiento;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.usuario = usuario;
    }

    public String getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(String alojamiento) {
        this.alojamiento = alojamiento;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
