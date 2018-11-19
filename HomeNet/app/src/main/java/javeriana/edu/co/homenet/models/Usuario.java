package javeriana.edu.co.homenet.models;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Usuario {
    private String id;
    private String nombre;
    // private String contraseña;  // Opcional
    private String urlImg;
    private int edad;
    private String tipoUsuario;
    private String correo;
    private String nacionalidad;
    private String sexo;
    private int telefono;
    private Map<String,Boolean> alojamientos = new HashMap<String,Boolean>();

    //Relaciones
    private List<String>opinionesAlojamiento;
    private List<String>opinionesGuia;
    private List<String>reservas;

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
        this.opinionesAlojamiento = new ArrayList<String>();
        this.opinionesGuia = new ArrayList<String>();
        this.reservas = new ArrayList<String>();
        this.alojamientos = new HashMap<String,Boolean>();
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

    public List<String> getOpinionesAlojamiento() {
        return opinionesAlojamiento;
    }

    public void setOpinionesAlojamiento(List<String> opinionesAlojamiento) {
        this.opinionesAlojamiento = opinionesAlojamiento;
    }

    public List<String> getOpinionesGuia() {
        return opinionesGuia;
    }

    public void setOpinionesGuia(List<String> opinionesGuia) {
        this.opinionesGuia = opinionesGuia;
    }

    public List<String> getReservas() {
        return reservas;
    }

    public void setReservas(List<String> reservas) {
        this.reservas = reservas;
    }

    public void agregarOpinionAlojamiento(String opinionAloja){
        this.opinionesAlojamiento.add(opinionAloja);
    }
    public void agregarOpinionGuia(String opinionGuia){
        this.opinionesGuia.add(opinionGuia);
    }
    public void agregarReserva(String reserva){
        this.reservas.add(reserva);
    }
}