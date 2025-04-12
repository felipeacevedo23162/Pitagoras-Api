package co.edu.elpoli.ces3.pitagorasapi.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import co.edu.elpoli.ces3.pitagorasapi.Modelo.Inscripcion;

public class InscripcionDAO {

    private Map<Integer, Inscripcion> inscripciones = new HashMap<>();

    // Método para registrar una inscripción
    public void registrarInscripcion(Inscripcion inscripcion) {
        inscripciones.put(inscripcion.getId(), inscripcion);
    }

    // Método para buscar inscripciones por carrera
    public List<Inscripcion> buscarPorCarrera(String carrera) {
        List<Inscripcion> resultado = new ArrayList<>();
        for (Inscripcion i : inscripciones.values()) {
            if (i.getCarrera().equalsIgnoreCase(carrera)) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    // Método para generar horarios optimizados evitando colisiones
    // Supone que el atributo 'asignatura' puede usarse como identificador para agrupar horarios
    // Aquí se usa un ejemplo simplificado: se agrupan asignaturas sin repetirlas
    public List<String> generarHorarioOptimizado(String documentoEstudiante) {
        Set<String> horario = new HashSet<>();
        for (Inscripcion inscripcion : inscripciones.values()) {
            if (inscripcion.getDocumento().equals(documentoEstudiante)
                    && inscripcion.getEstado().equalsIgnoreCase("Activa")) {
                horario.add(inscripcion.getAsignatura());
            }
        }
        return new ArrayList<>(horario);
    }

    // Extra: obtener todas las inscripciones
    public List<Inscripcion> obtenerTodas() {
        return new ArrayList<>(inscripciones.values());
    }
}
