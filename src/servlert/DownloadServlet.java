package servlert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download")
public class DownloadServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String name = req.getParameter("name");
		String range = req.getParameter("range");
		String path = "C:/WebInfos/" + name;
		File file = new File(path);
		long length = file.length();
		resp.setContentLength((int) length);
		OutputStream out = resp.getOutputStream();

		if (range == null || "".equals(range.trim())) {
			FileInputStream stream = new FileInputStream(file);
			int count = -1;
			byte[] buffer = new byte[1024];
			while ((count = stream.read(buffer)) != -1) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.write(buffer, 0, count);
				out.flush();
			}
			stream.close();
			out.close();
		} else {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(Long.valueOf(range));
			int count = -1;
			byte[] buffer = new byte[1024];
			while ((count = raf.read(buffer)) != -1) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.write(buffer, 0, count);
				out.flush();
			}
			raf.close();
			out.close();
		}

	}
}
