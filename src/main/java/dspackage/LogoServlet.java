package dspackage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/favicon")
public class LogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String imagePath = getServletContext().getRealPath("/resources/favicon.jpeg");
		//response.setContentType("image/png");

		//String filePath = "./favicon.png";

		String mimeType = getServletContext().getMimeType(imagePath);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);

		try (FileInputStream fis = new FileInputStream(imagePath);
				BufferedInputStream bis = new BufferedInputStream(fis);
				OutputStream out = response.getOutputStream()) {
			byte[] buffer = new byte[74987];
			int bytesRead;
			while ((bytesRead = bis.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		} 
	}

}