package admin;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import db.DBConnection;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.*;

@WebServlet("/msg_add")
@MultipartConfig
public class msg_add extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("Content type is not multipart/form-data");
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024); // 1MB

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(1024 * 1024 * 10); // 10MB

        String uploadPath = getServletContext().getRealPath("") + File.separator + "msg";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        try {
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        String sql = "INSERT INTO image (path) VALUES (?)";

                        try (Connection connection = DBConnection.getConnection()) {
                            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                                statement.setString(1, fileName);


                                int row = statement.executeUpdate();

                                if(row > 0) {
                                }
                                connection.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();

                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        // Saves the file on disk
                        item.write(storeFile);

                        // Convert and save as .msg file
                        File msgFile = new File(uploadPath + File.separator + "image.msg");
                        try (FileOutputStream fos = new FileOutputStream(msgFile);
                             FileInputStream fis = new FileInputStream(storeFile)) {
                            IOUtils.copy(fis, fos);
                        }

                        request.setAttribute("message", "File uploaded successfully!");
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
        RequestDispatcher rd = request.getRequestDispatcher("admin_msg.jsp");
        rd.forward(request, response);

    }


}