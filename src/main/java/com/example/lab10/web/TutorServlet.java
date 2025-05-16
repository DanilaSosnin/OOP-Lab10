package com.example.lab10.web;

import com.example.lab10.model.Tutor;
import com.example.lab10.util.DBUtil;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/tutors")
public class TutorServlet extends HttpServlet {
    private static final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (Connection conn = DBUtil.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "tutors", new String[]{"TABLE"});
            if (!tables.next()) {
                sendError(resp, "Таблица не найдена", 500);
                return;
            }

            // Запрос данных
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM tutors")) {

                List<Tutor> tutors = new ArrayList<>();
                while (rs.next()) {
                    Tutor tutor = new Tutor(
                            rs.getInt("ID"),
                            rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("SUBJECT"),
                            rs.getInt("EXPERIENCE"),
                            rs.getDouble("PRICE"),
                            rs.getString("PHONENUMBER")
                    );
                    tutors.add(tutor);
                }

                if ("edit".equals(req.getParameter("action"))) {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Tutor editTutor = tutors.stream()
                            .filter(t -> t.getId() == id)
                            .findFirst()
                            .orElse(null);

                    if (editTutor != null) {
                        req.setAttribute("editTutor", editTutor);
                    }
                }

                req.setAttribute("tutors", tutors);
            }
        } catch (SQLException e) {
            sendError(resp, "Ошибка базы данных: " + e.getMessage(), 500);
            return;
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String subject = req.getParameter("subject");
        String experienceStr = req.getParameter("experience");
        String priceStr = req.getParameter("price");
        String phone = req.getParameter("phoneNumber");
        req.setAttribute("redirected", "true");

        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(subject)
                || isEmpty(experienceStr) || isEmpty(priceStr) || isEmpty(phone)) {
            sendError(resp, "Все поля обязательны для заполнения", 400);
            return;
        }

        try {
            Tutor tutor = new Tutor();
            tutor.setFirstName(firstName);
            tutor.setLastName(lastName);
            tutor.setSubject(subject);
            tutor.setExperience(Integer.parseInt(experienceStr));
            tutor.setPrice(Double.parseDouble(priceStr));
            tutor.setPhoneNumber(phone);

            String sql = "INSERT INTO tutors (FIRSTNAME, LASTNAME, SUBJECT, EXPERIENCE, PRICE, PHONENUMBER) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, tutor.getFirstName());
                pstmt.setString(2, tutor.getLastName());
                pstmt.setString(3, tutor.getSubject());
                pstmt.setInt(4, tutor.getExperience());
                pstmt.setDouble(5, tutor.getPrice());
                pstmt.setString(6, tutor.getPhoneNumber());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    sendError(resp, "Не удалось создать запись", 500);
                    return;
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tutor.setId(generatedKeys.getInt(1));
                    }
                }

                resp.sendRedirect(req.getContextPath() + "/tutors");

            } catch (SQLException e) {
                sendError(resp, "Ошибка базы данных: " + e.getMessage(), 500);
            }

        } catch (NumberFormatException e) {
            sendError(resp, "Некорректный формат числа", 400);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String subject = req.getParameter("subject");
        String experienceStr = req.getParameter("experience");
        String priceStr = req.getParameter("price");
        String phoneNumber = req.getParameter("phoneNumber");

        if (isEmpty(idParam) || isEmpty(firstName) || isEmpty(lastName)
                || isEmpty(subject) || isEmpty(experienceStr)
                || isEmpty(priceStr) || isEmpty(phoneNumber)) {
            sendError(resp, "Все поля обязательны", 400);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            int experience = Integer.parseInt(experienceStr);
            double price = Double.parseDouble(priceStr);

            Tutor tutor = new Tutor(id, firstName, lastName, subject, experience, price, phoneNumber);

            String sql = "UPDATE tutors SET FIRSTNAME=?, LASTNAME=?, SUBJECT=?, "
                    + "EXPERIENCE=?, PRICE=?, PHONENUMBER=? WHERE ID=?";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, tutor.getFirstName());
                pstmt.setString(2, tutor.getLastName());
                pstmt.setString(3, tutor.getSubject());
                pstmt.setInt(4, tutor.getExperience());
                pstmt.setDouble(5, tutor.getPrice());
                pstmt.setString(6, tutor.getPhoneNumber());
                pstmt.setInt(7, tutor.getId());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    sendError(resp, "Запись не найдена", 404);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/tutors");
                }
            }
        } catch (NumberFormatException e) {
            sendError(resp, "Некорректный формат числа", 400);
        } catch (SQLException e) {
            sendError(resp, "Ошибка обновления: " + e.getMessage(), 500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idParam = req.getParameter("id");

        try {
            int id = Integer.parseInt(idParam);
            String sql = "DELETE FROM tutors WHERE ID = ?";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    sendError(resp, "Tutor not found",
                            HttpServletResponse.SC_NOT_FOUND);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            }
        } catch (NumberFormatException e) {
            sendError(resp, "Invalid ID format",
                    HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            sendError(resp, "Delete error: " + e.getMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.sendRedirect(req.getContextPath() + "/tutors");
    }

    private void setTutorParameters(PreparedStatement pstmt, Tutor tutor)
            throws SQLException {
        pstmt.setString(1, tutor.getFirstName());
        pstmt.setString(2, tutor.getLastName());
        pstmt.setString(3, tutor.getSubject());
        pstmt.setInt(4, tutor.getExperience());
        pstmt.setDouble(5, tutor.getPrice());
        pstmt.setString(6, tutor.getPhoneNumber());
    }

    private void sendError(HttpServletResponse resp, String message, int status)
            throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}