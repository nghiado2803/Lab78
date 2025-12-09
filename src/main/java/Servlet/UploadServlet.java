package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/api/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class UploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String name = "";
        String type = "";
        long size = 0;

        for (Part part : req.getParts()) {
            if (part.getName().equals("file") && part.getSize() > 0) {
                name = getFileName(part);
                type = part.getContentType();
                size = part.getSize();

                // Lưu file vào thư mục uploads (tạo nếu chưa có)
                String uploadPath = getServletContext().getRealPath("") + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();

                part.write(uploadPath + File.separator + name);
            }
        }

        // Trả về JSON đúng như đề bài
        String json = String.format("""
            {
                "name": "%s",
                "type": "%s",
                "size": %d
            }
            """, name, type, size);

        resp.getWriter().print(json);
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "unknown";
    }
}