package co.edu.elpoli.ces3.pitagorasapi.Servlets;

import co.edu.elpoli.ces3.pitagorasapi.DAO.InscripcionDAO;
import co.edu.elpoli.ces3.pitagorasapi.Modelo.Inscripcion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.google.gson.Gson;

@WebServlet("/inscripciones/*")
public class InscripcionServlet extends HttpServlet {

    private InscripcionDAO inscripcionDAO = new InscripcionDAO();
    private Gson gson = new Gson();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            Inscripcion inscripcion = gson.fromJson(req.getReader(), Inscripcion.class);

            // Validación 1: No más de 7 inscripciones por estudiante
            long conteo = inscripcionDAO.obtenerTodas().stream()
                    .filter(i -> i.getDocumento().equals(inscripcion.getDocumento()) && i.getEstado().equalsIgnoreCase("Activa"))
                    .count();

            if (conteo >= 7) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("El estudiante ya tiene 7 inscripciones activas.");
                return;
            }

            // Validación 2: No permitir duplicados de estudiante + asignatura
            boolean yaInscrito = inscripcionDAO.obtenerTodas().stream()
                    .anyMatch(i -> i.getDocumento().equals(inscripcion.getDocumento())
                            && i.getAsignatura().equalsIgnoreCase(inscripcion.getAsignatura()));

            if (yaInscrito) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("El estudiante ya está inscrito en esta asignatura.");
                return;
            }

            // Calcular prioridad
            double prioridad = calcularPrioridad(inscripcion);
            inscripcion.setPrioridad((int) Math.round(prioridad));

            inscripcionDAO.registrarInscripcion(inscripcion);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(inscripcion));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        if (path.equals("/carrera")) {
            String carrera = req.getParameter("nombre");
            List<Inscripcion> porCarrera = inscripcionDAO.buscarPorCarrera(carrera);
            out.write(gson.toJson(porCarrera));
        } else if (path.equals("/priorizadas")) {
            List<Inscripcion> todas = inscripcionDAO.obtenerTodas();
            for (Inscripcion i : todas) {
                i.setPrioridad((int) Math.round(calcularPrioridad(i)));
            }

            todas.sort((a, b) -> Integer.compare(b.getPrioridad(), a.getPrioridad()));
            out.write(gson.toJson(todas));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("{\"error\":\"Ruta no válida\"}");
        }
    }

    private double calcularPrioridad(Inscripcion inscripcion) {
        double promedio = inscripcion.getPromedioAcumulado();
        double creditos = inscripcion.getCreditos();
        long diasDesdeInscripcion = 0;

        try {
            LocalDate fecha = LocalDate.parse(inscripcion.getFechaInscripcion(), formatter);
            diasDesdeInscripcion = java.time.temporal.ChronoUnit.DAYS.between(fecha, LocalDate.now());
        } catch (Exception e) {
            diasDesdeInscripcion = 0; // Fallback por formato incorrecto
        }

        return (promedio * 0.6) + ((creditos / 10.0) * 0.3) + (diasDesdeInscripcion * 0.1);
    }
}
