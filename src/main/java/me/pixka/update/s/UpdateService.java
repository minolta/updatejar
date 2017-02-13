package me.pixka.update.s;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.pixka.c.HttpControl;
import me.pixka.data.ISODateAdapter;
import me.pixka.device.c.UploadpifwControl;
import me.pixka.device.d.Pifw;
import me.pixka.device.s.ConfigvalueService;

@Service
public class UpdateService {

	private static Gson gg = new GsonBuilder().registerTypeAdapter(Date.class, new ISODateAdapter()).create();
	@Autowired
	private HttpControl http;
	@Autowired
	private UploadpifwControl up;
	@Autowired
	private ConfigvalueService configserver;
	private String host = "http://localhost:3333";
	private String tmp = "tmp";
	private boolean f = false;

	@Async
	public void run() throws IOException, NoSuchAlgorithmException, InterruptedException {

		while (true) {
			try {

				String version = new String(Files.readAllBytes(Paths.get("version")));
				String target = new String(Files.readAllBytes(Paths.get("targetfile")));
				String runscript = new String(Files.readAllBytes(Paths.get("runscript")));
				String hostconfig = new String(Files.readAllBytes(Paths.get("host")));
				String appname = new String(Files.readAllBytes(Paths.get("appname")));

				//
				//
				// String version = configserver.get("version", "0");
				//
				// String target = new
				// String(Files.readAllBytes(Paths.get("targetfile")));
				// String runscript = new
				// String(Files.readAllBytes(Paths.get("runscript")));
				// String hostconfig = new
				// String(Files.readAllBytes(Paths.get("host")));
				// String appname = new
				// String(Files.readAllBytes(Paths.get("appname")));
				//

				this.host = hostconfig.replaceAll("\r", "").replaceAll("\n", "");
				target = target.replaceAll("\r", "").replaceAll("\n", "");
				appname = appname.replaceAll("\r", "").replaceAll("\n", "");
				version = version.replaceAll("\r", "").replaceAll("\n", "");
				runscript = runscript.replaceAll("\r", "").replaceAll("\n", "");
				String toget = host + "/pifwinfo/" + version + "/" + appname;
				String re = http.get(toget);

				System.out.println("Version:" + version + " Target:" + target + " Toget FW: " + toget);

				Pifw info = gg.fromJson(re, Pifw.class);

				if (info != null && f == false) {
					f = true;
					System.out.println("Have new FW " + info.getVerno() + " checksum:" + info.getChecksum());

					
					URL url = new URL(host + "/pifw/" + info.getVerno() + "/" + appname);
					String filepath = tmp + File.separatorChar + info.getVerno();
					File file = new File(filepath);
					FileUtils.copyURLToFile(url, file);

					String md5 = up.md5(filepath);
					System.out.println(" MD5 Check: Server :" + info.getChecksum() + " Save local:" + md5);
					if (info.getChecksum().equals(md5)) {
						copy(filepath, target);
						System.out.println("Copy FW");
						restart(runscript);
						System.out.println("Restart service");
						f = false;
						//ต้อง copy เสร็จก่อนค่อยเปลียน VERSION
						Path path = Paths.get("version");
						try (BufferedWriter writer = Files.newBufferedWriter(path)) {
							writer.write(info.getVerno());
						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				f = false;
				Thread.sleep(60000);
			}
		}

	}

	/**
	 * copy Fw to program to run
	 * 
	 * @throws IOException
	 */
	public void copy(String src, String des) throws IOException {
		System.out.println(src + " to " + des);
		FileUtils.copyFile(new File(src), new File(des));
	}

	/**
	 * ใช้สำหรับ run script restart
	 * 
	 * @param script
	 * @throws IOException
	 */
	public void restart(String script) throws IOException {
		Runtime.getRuntime().exec(script);
	}

}
