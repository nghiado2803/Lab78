package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.*;

@WebServlet("/api/employees/*")
public class EmployeeRestServlet extends HttpServlet {

    // Dữ liệu giả lập (thay vì DB)
    private static final List<Map<String, Object>> employees = new ArrayList<>();
    private static final Gson gson = new Gson();

    static {
        Map<String, Object> e1 = new HashMap<>();
        e1.put("id", "NV001");
        e1.put("name", "Nguyễn Văn Tèo");
        e1.put("gender", true);
        e1.put("salary", 950.5);
        employees.add(e1);

        Map<String, Object> e2 = new HashMap<>();
        e2.put("id", "NV002");
        e2.put("name", "Trần Thị Nở");
        e2.put("gender", false);
        e2.put("salary", 1200.0);
        employees.add(e2);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo(); // ví dụ: /NV001 hoặc null

        if (pathInfo == null || pathInfo.equals("/")) {
            // GET /api/employees → trả về danh sách
            resp.getWriter().print(gson.toJson(employees));
        } else {
            // GET /api/employees/NV001 → trả về 1 nhân viên
            String id = pathInfo.substring(1);
            Map<String, Object> emp = findById(id);
            if (emp != null) {
                resp.getWriter().print(gson.toJson(emp));
            } else {
                resp.setStatus(404);
                resp.getWriter().print("{\"error\":\"Không tìm thấy nhân viên\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);
        String id = json.get("id").getAsString();

        if (findById(id) != null) {
            resp.setStatus(400);
            resp.getWriter().print("{\"error\":\"ID đã tồn tại!\"}");
            return;
        }

        Map<String, Object> newEmp = new HashMap<>();
        newEmp.put("id", id);
        newEmp.put("name", json.get("name").getAsString());
        newEmp.put("gender", json.get("gender").getAsBoolean());
        newEmp.put("salary", json.get("salary").getAsDouble());

        employees.add(newEmp);
        resp.setStatus(201);
        resp.getWriter().print(gson.toJson(newEmp));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(400);
            return;
        }

        String id = pathInfo.substring(1);
        Map<String, Object> emp = findById(id);
        if (emp == null) {
            resp.setStatus(404);
            resp.getWriter().print("{\"error\":\"Không tìm thấy\"}");
            return;
        }

        JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);
        emp.put("name", json.get("name").getAsString());
        emp.put("gender", json.get("gender").getAsBoolean());
        emp.put("salary", json.get("salary").getAsDouble());

        resp.getWriter().print(gson.toJson(emp));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(400);
            return;
        }

        String id = pathInfo.substring(1);
        boolean removed = employees.removeIf(e -> e.get("id").equals(id));

        if (removed) {
            resp.setStatus(200);
            resp.getWriter().print("{\"message\":\"Xóa thành công\"}");
        } else {
            resp.setStatus(404);
            resp.getWriter().print("{\"error\":\"Không tìm thấy\"}");
        }
    }

    private Map<String, Object> findById(String id) {
        return employees.stream()
                .filter(e -> e.get("id").equals(id))
                .findFirst()
                .orElse(null);
    }
}
 //