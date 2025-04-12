package co.edu.elpoli.ces3.pitagorasapi.Modelo;

import java.util.Arrays;

public class Curso {
    //Atributos: id (int), nombre (String), código (String), profesor (String), cupoMaximo (int), estudiantesInscritos (int), facultad (String), prerequisitos (ArrayList<String>), nivel (int), fechaInicio (String)
    private int id;
    private String nombre;
    private String codigo;
    private String profesor;
    private int cupoMaximo;
    private int estudiantesInscritos;
    private String facultad;
    private String[] prerequisitos;
    private int nivel;
    private String fechaInicio;
    //Constructor
    public Curso(int id, String nombre, String codigo, String profesor, int cupoMaximo, int estudiantesInscritos, String facultad, String[] prerequisitos, int nivel, String fechaInicio) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.profesor = profesor;
        this.cupoMaximo = cupoMaximo;
        this.estudiantesInscritos = estudiantesInscritos;
        this.facultad = facultad;
        this.prerequisitos = prerequisitos;
        this.nivel = nivel;
        this.fechaInicio = fechaInicio;
    }
    //Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getProfesor() {
        return profesor;
    }
    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
    public int getCupoMaximo() {
        return cupoMaximo;
    }
    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
    public int getEstudiantesInscritos() {
        return estudiantesInscritos;
    }
    public void setEstudiantesInscritos(int estudiantesInscritos) {
        this.estudiantesInscritos = estudiantesInscritos;
    }
    public String getFacultad() {
        return facultad;
    }
    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }
    public String[] getPrerequisitos() {
        return prerequisitos;
    }
    public void setPrerequisitos(String[] prerequisitos) {
        this.prerequisitos = prerequisitos;
    }
    public int getNivel() {
        return nivel;
    }
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    //Método toString
    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", profesor='" + profesor + '\'' +
                ", cupoMaximo=" + cupoMaximo +
                ", estudiantesInscritos=" + estudiantesInscritos +
                ", facultad='" + facultad + '\'' +
                ", prerequisitos=" + Arrays.toString(prerequisitos) +
                ", nivel=" + nivel +
                ", fechaInicio='" + fechaInicio + '\'' +
                '}';
    }
}
