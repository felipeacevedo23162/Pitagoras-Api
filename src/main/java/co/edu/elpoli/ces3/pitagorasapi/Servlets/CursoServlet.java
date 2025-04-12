package co.edu.elpoli.ces3.pitagorasapi.Servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import co.edu.elpoli.ces3.pitagorasapi.DAO.CursoDAO;
import co.edu.elpoli.ces3.pitagorasapi.Modelo.Curso;

@WebServlet("/cursos/*")
public class CursoServlet extends HttpServlet {

    private CursoDAO cursoDAO = new CursoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Soporta datos en formato JSON
        Curso curso = gson.fromJson(req.getReader(), Curso.class);

        // Validación: No permitir cursos con el mismo código
        if (cursoDAO.buscarPorCodigo(curso.getCodigo()) != null) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("Ya existe un curso con este código.");
            return;
        }

        // Validación: Cupo máximo debe ser mayor a cero
        if (curso.getCupoMaximo() <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("El cupo máximo debe ser mayor que cero.");
            return;
        }

        // Validación: Todos los prerequisitos deben existir
        boolean todosExisten = Arrays.stream(curso.getPrerequisitos())
                .allMatch(cod -> cursoDAO.buscarPorCodigo(cod) != null);

        if (!todosExisten) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Uno o más prerequisitos no existen en el sistema.");
            return;
        }

        cursoDAO.agregarCurso(curso);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(curso));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        if (path.equals("/facultad")) {
            String nombreFacultad = req.getParameter("nombre");
            List<Curso> cursos = cursoDAO.buscarPorFacultad(nombreFacultad);
            out.write(gson.toJson(cursos));

        } else if (path.equals("/ruta")) {
            String codigoCurso = req.getParameter("codigo");
            List<Curso> ruta = cursoDAO.encontrarRutaAprendizaje(codigoCurso);

            // Ordenar por nivel
            ruta.sort(Comparator.comparingInt(Curso::getNivel));
            out.write(gson.toJson(ruta));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("{\"error\":\"Ruta no válida\"}");
        }
    }
}
