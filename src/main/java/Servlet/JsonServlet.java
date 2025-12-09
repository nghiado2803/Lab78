package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/nhanvien")
public class JsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Thiết lập kiểu trả về là JSON + UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Viết đúng JSON theo đề bài
        String json = """
            {
   "manv": "TeoNV",
   "hoTen": "Nguyễn Văn Tèo",
   "gioiTinh": true,
   "luong": 950.5
 }
 """;

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}