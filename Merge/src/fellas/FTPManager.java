package fellas;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FTPManager {
	private String host;
	private String user;
	private String password;

	public FTPManager(String host, String user, String password) {
		this.host = host;
		this.user = user;
		this.password = password;
	}

	public synchronized boolean uploadFile(String localFile, String remoteFile) {
		try {
			URL url = new URL("ftp://" + user + ":" + password + "@" + host
					+ "/www/" + remoteFile + ";type=i");
			System.out.println("ftp://" + user + ":" + password + "@" + host
					+ "/www/" + remoteFile + ";type=i");
			URLConnection m_client = url.openConnection();

			InputStream is = new FileInputStream(localFile);
			BufferedInputStream bis = new BufferedInputStream(is);
			OutputStream os = m_client.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			byte[] buffer = new byte[1024];
			int readCount;

			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bos.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public synchronized boolean downloadFile(String localFile, String remoteFile) {
		try {
			URL url = new URL("ftp://" + user + ":" + password + "@" + host
					+ "/www/" + remoteFile + ";type=i");
			URLConnection m_client = url.openConnection();

			InputStream is = m_client.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			OutputStream os = new FileOutputStream(localFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);

			byte[] buffer = new byte[1024];
			int readCount;

			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bos.close();
			is.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		FTPManager up = new FTPManager("diana.netsons.org", "diananet",
				"password1234");
		// up.uploadFile("ct.jpg", "ct.jpg");
		up.downloadFile("prova.jpg", "ct.jpg");

	}

}