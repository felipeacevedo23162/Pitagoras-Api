package co.edu.elpoli.ces3.pitagorasapi.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.elpoli.ces3.pitagorasapi.Modelo.Curso;

public class CursoDAO {

    private Map<Integer, Curso> cursos = new HashMap<>();

    // Método para agregar un curso
    public void agregarCurso(Curso curso) {
        cursos.put(curso.getId(), curso);
    }

    // Método para buscar cursos por facultad
    public List<Curso> buscarPorFacultad(String facultad) {
        List<Curso> resultado = new ArrayList<>();
        for (Curso curso : cursos.values()) {
            if (curso.getFacultad().equalsIgnoreCase(facultad)) {
                resultado.add(curso);
            }
        }
        return resultado;
    }

    // Método para encontrar rutas de aprendizaje
    public List<Curso> encontrarRutaAprendizaje(String codigoCurso) {
        List<Curso> ruta = new ArrayList<>();
        Curso curso = buscarPorCodigo(codigoCurso);
        if (curso != null) {
            buscarPrerequisitosRecursivo(curso, ruta);
        }
        return ruta;
    }

    // Método auxiliar recursivo
    private void buscarPrerequisitosRecursivo(Curso curso, List<Curso> ruta) {
        for (String codPre : curso.getPrerequisitos()) {
            Curso preCurso = buscarPorCodigo(codPre);
            if (preCurso != null && !ruta.contains(preCurso)) {
                buscarPrerequisitosRecursivo(preCurso, ruta);
                ruta.add(preCurso);
            }
        }
    }

    // Método para buscar curso por código
    public Curso buscarPorCodigo(String codigo) {
        for (Curso curso : cursos.values()) {
            if (curso.getCodigo().equalsIgnoreCase(codigo)) {
                return curso;
            }
        }
        return null;
    }

    // Extra: Obtener todos los cursos
    public List<Curso> obtenerTodosLosCursos() {
        return new ArrayList<>(cursos.values());
    }
}
