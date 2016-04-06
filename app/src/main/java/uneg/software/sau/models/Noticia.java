package uneg.software.sau.models;

import java.util.ArrayList;

/**
 * Created by Jhonny on 25/2/2016.
 */
public class Noticia {

    ArrayList<String> fotos;
    String titulo;
    String area;
    String id_noticia;
    private String nombreUser;
    private String fecha;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    double lat;
    double lon;


    public Noticia(ArrayList<String> fotos, String titulo, String area, String descripcion) {
        this.fotos = fotos;
        this.titulo = titulo;
        this.area = area;
        this.descripcion = descripcion;
        this.lat=-8.6;
        this.lon=62.6;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<String> fotos) {
        this.fotos = fotos;
    }

    String descripcion;

    public String getId_noticia() {
        return id_noticia;
    }

    public String getFirstFoto() {
        if(fotos.size()==0)return "";
        return fotos.get(0);
    }


    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
