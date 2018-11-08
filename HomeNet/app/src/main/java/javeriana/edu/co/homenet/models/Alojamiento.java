package javeriana.edu.co.homenet.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javeriana.edu.co.homenet.utils.DateFormater;
import javeriana.edu.co.homenet.utils.DistanceFunc;

public class Alojamiento{
    private String id;
    private List<String> urlImgs;
    private long precio;
    private String tipo;
    private String descripcion;
    private String nombre;

    private double dist;

    // relaciones
    private String anfitrion;
    private List<Disponibilidad> disponibilidades;
    private Ubicacion ubicacion;
    private List<Reserva> reservas;

    public Alojamiento()
    {
        this.reservas = new ArrayList<Reserva>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<String> getUrlImgs() {
        return urlImgs;
    }

    public void setUrlImgs(List<String> urlImgs) {
        this.urlImgs = urlImgs;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    public String getAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(String anfitrion) {
        this.anfitrion = anfitrion;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public boolean estaDisponible(String fechaInicio, String fechaFin){
        boolean disp = false;
        Date fi;
        Date ff;
        if(fechaInicio==""){
            fi = DateFormater.today();
        }
        else{
            fi = DateFormater.stringToDate(fechaInicio);
        }
        if(fechaFin==""){
            ff = DateFormater.stringToDate("01/01/3000");
        }
        else{
            ff = DateFormater.stringToDate(fechaFin);
        }

        for (Disponibilidad fechasDisp: disponibilidades) {
            Date fia = DateFormater.stringToDate(fechasDisp.getFechaInicio());
            Date ffa = DateFormater.stringToDate(fechasDisp.getFechaFin());
            Log.d("Alojamiento", fia.toString());
            if((fi.after(fia) && ff.before(ffa)) || (fi.equals(fia) && ff.before(ffa))
                    || (fi.after(fia) && ff.equals(ffa)) || (fi.equals(fia) && ff.equals(ffa))){
                disp = true;
                for (Reserva reserva: reservas) {
                    Date fir = DateFormater.stringToDate(reserva.getFechaInicio());
                    Date ffr = DateFormater.stringToDate(reserva.getFechaFin());
                    if((fi.after(fir) && ff.before(ffr)) || (fi.equals(fir) && ff.before(ffr))
                            || (fi.after(fir) && ff.equals(ffr)) || (fi.equals(fir) && ff.equals(ffr))){
                        disp = false;
                    }
                }
            }
        }

        return disp;
    }

    public boolean tienePalabra(String palabra){
        boolean tiene = false;
        if(nombre != null && nombre.toLowerCase().contains(palabra.toLowerCase())) {
            tiene = true;
        }
        if(tipo != null && tipo.toLowerCase().contains(palabra.toLowerCase())) {
            tiene = true;
        }
        if(descripcion!=null && descripcion.toLowerCase().contains(palabra.toLowerCase())) {
            tiene = true;
        }
        if(anfitrion!=null && anfitrion.toLowerCase().contains(palabra.toLowerCase())) {
            tiene = true;
        }
        return tiene;
    }

    public boolean estaCerca(double lat1, double long1, double km){
        if(ubicacion!=null){
            double distance = DistanceFunc.distance(lat1,long1,ubicacion.getLatitude(),ubicacion.getLongitude());
            if(distance<=km){
                return true;
            }
            return false;
        }
        return  false;
    }

    public void initDist(double lat1, double long1){
        if(ubicacion!=null)
            dist =  DistanceFunc.distance(lat1,long1,ubicacion.getLatitude(),ubicacion.getLongitude());
    }
}