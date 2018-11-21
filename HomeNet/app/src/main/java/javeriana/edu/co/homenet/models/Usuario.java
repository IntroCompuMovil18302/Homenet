package javeriana.edu.co.homenet.models;

import java.util.Map;

public class Usuario {
    private String id;
    private String nombre;
    private String urlImg;
    private int edad;
    private String tipoUsuario;
    private String correo;
    private String nacionalidad;
    private String sexo;
    private int telefono;
    private int calificacion;
    private Map<String,Boolean> alojamientos;

    //Relaciones
    private Map<String,Boolean>opinionesAlojamiento;
    private Map<String,Boolean> opinionesGuia;
    private Map<String,Boolean> reservas;

    public Usuario() {}

    public Usuario(String nombre, String urlImg, int edad, String tipoUsuario, String correo,
                   String nacionalidad, String sexo) {
        this.nombre = nombre;
        this.urlImg = urlImg;
        this.edad = edad;
        this.tipoUsuario = tipoUsuario;
        this.correo = correo;
        this.nacionalidad = nacionalidad;
        this.sexo = sexo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int c) {
        this.calificacion = calificacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Map<String,Boolean> getAlojamientos() {
        return alojamientos;
    }

    public void setAlojamientos(Map<String,Boolean> alojamientos) {
        this.alojamientos = alojamientos;
    }
    

    public void agregarElemento(String aloj,Boolean id){
        this.alojamientos.put(aloj,id);
    }

    public Map<String,Boolean> getOpinionesAlojamiento() {
        return opinionesAlojamiento;
    }

    public void setOpinionesAlojamiento(Map<String,Boolean> opinionesAlojamiento) {
        this.opinionesAlojamiento = opinionesAlojamiento;
    }

    public Map<String,Boolean> getOpinionesGuia() {
        return opinionesGuia;
    }

    public void setOpinionesGuia(Map<String,Boolean> opinionesGuia) {
        this.opinionesGuia = opinionesGuia;
    }

    public Map<String,Boolean> getReservas() {
        return reservas;
    }

    public void setReservas(Map<String,Boolean> reservas) {
        this.reservas = reservas;
    }

    public void agregarOpinionAlojamiento(String opinionAloja, Boolean b){
        this.opinionesAlojamiento.put(opinionAloja,b);
    }
    public void agregarOpinionGuia(String opinionGuia, Boolean b){
        this.opinionesGuia.put(opinionGuia, b);
    }
    public void agregarReserva(String reserva, Boolean b){
        this.reservas.put(reserva,b);
    }
}
