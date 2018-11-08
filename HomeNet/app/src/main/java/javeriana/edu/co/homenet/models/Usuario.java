package javeriana.edu.co.homenet.models;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> alojamientos=new ArrayList<String>();

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
        this.alojamientos= new ArrayList<String>();
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<String> getAlojamientos() {
        return alojamientos;
    }

    public void setAlojamientos(List<String> alojamientos) {
        this.alojamientos = alojamientos;
    }
    public void agregarElemento(String id){
        this.alojamientos.add(id);
    }
}
