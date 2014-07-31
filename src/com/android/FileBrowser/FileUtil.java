package com.android.FileBrowser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

/** 文件处理工具类 **/
public class FileUtil {

	public static final String strFileName = "/calllog.txt";

	/** 获取SD路径 **/
	public static String getSDPath() {
		// 判断sd卡是否存在
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.getPath();
		}
		return "/sdcard";
	}

	/** 获取文件信息 **/
	public static FileInfo getFileInfo(File f) {
		FileInfo info = new FileInfo();
		info.Name = f.getName();
		info.IsDirectory = f.isDirectory();
		calcFileContent(info, f);
		return info;
	}

	/** 计算文件内容 **/
	private static void calcFileContent(FileInfo info, File f) {
		if (f.isFile()) {
			info.Size += f.length();
		}
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					File tmp = files[i];
					if (tmp.isDirectory()) {
						info.FolderCount++;
					} else if (tmp.isFile()) {
						info.FileCount++;
					}
					if (info.FileCount + info.FolderCount >= 10000) { // 超过一万不计算
						break;
					}
					calcFileContent(info, tmp);
				}
			}
		}
	}

	/** 转换文件大小 **/
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = fileS + " B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + " K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + " M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + " G";
		}
		return fileSizeString;
	}

	/** 合并路径 **/
	public static String combinPath(String path, String fileName) {
		return path + (path.endsWith(File.separator) ? "" : File.separator)
				+ fileName;
	}

	/** 复制文件 **/
	public static boolean copyFile(File src, File tar) throws Exception {
		if (src.isFile()) {
			InputStream is = new FileInputStream(src);
			OutputStream op = new FileOutputStream(tar);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(op);
			byte[] bt = new byte[1024 * 8];
			int len = bis.read(bt);
			while (len != -1) {
				bos.write(bt, 0, len);
				len = bis.read(bt);
			}
			bis.close();
			bos.close();
		}
		if (src.isDirectory()) {
			File[] f = src.listFiles();
			tar.mkdir();
			for (int i = 0; i < f.length; i++) {
				copyFile(f[i].getAbsoluteFile(), new File(tar.getAbsoluteFile()
						+ File.separator + f[i].getName()));
			}
		}
		return true;
	}

	/** 移动文件 **/
	public static boolean moveFile(File src, File tar) throws Exception {
		if (copyFile(src, tar)) {
			deleteFile(src);
			return true;
		}
		return false;
	}

	/** 删除文件 **/
	public static void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					deleteFile(files[i]);
				}
			}
		}
		f.delete();
	}

	/** 获取MIME类型 **/
	public static String getMIMEType(String name) {
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("apk")) {
			return "application/vnd.android.package-archive";
		} else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp")
				|| end.equals("rmvb")) {
			type = "video";
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("txt") || end.equals("log")) {
			type = "text";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	/**
	 * @author: liyb
	 * @Title: saveLogToFile
	 * @Description: 鍐欐棩蹇楀唴瀹瑰埌鏂囦欢锟?
	 * @param content
	 *            鏃ュ織鍐呭
	 * @date: 2012-10-15 涓嬪崍02:26:01
	 */
	public void saveLogToFile(String content) {
		File file = new File(strFileName);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write("\n");
			out.write(content);
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public void creatFile(String linkPath) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(linkPath));
			out.write("");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFileContext(String linkPath, String context,
			boolean encoded) {

		File file = new File(linkPath);
		if (!file.exists()) {
			this.creatFile(linkPath);
		}

		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(linkPath, "rw");
			rf.seek(rf.length());
			if (encoded) {
				context = new String(context.getBytes("GBK"), "ISO-8859-1");
			}
			rf.writeBytes(context + "\n");
			rf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String copyFile(String old_path, String new_path) {
		String name = null;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {

			bis = new BufferedInputStream(new FileInputStream(
					new File(old_path)));
			bos = new BufferedOutputStream(new FileOutputStream(new File(
					new_path)));

			int code = 0;
			while ((code = bis.read()) != -1) {
				bos.write(code);
				bos.flush();
			}

			if (bis != null) {
				bis.close();
				bis = null;
			} else {
				bis = null;
			}

			if (bos != null) {
				bos.close();
				bos = null;
			} else {
				bos = null;
			}

			File file = new File(new_path);
			if (file.exists()) {
				name = file.getName();
			}
		} catch (Exception e) {
			if (bis != null) {
				try {
					bis.close();
					bis = null;
				} catch (IOException e1) {
					bis = null;
				}
			} else {
				bis = null;
			}

			if (bos != null) {
				try {
					bos.close();
					bos = null;
				} catch (IOException e1) {
					bos = null;
				}
			} else {
				bos = null;
			}
			e.printStackTrace();
		}
		return name;
	}

	public String copyFile(InputStream is, String new_path) {
		String name = null;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {

			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(new FileOutputStream(new File(
					new_path)));

			int code = 0;
			while ((code = bis.read()) != -1) {
				bos.write(code);
				bos.flush();
			}

			if (bis != null) {
				bis.close();
				bis = null;
			} else {
				bis = null;
			}

			if (bos != null) {
				bos.close();
				bos = null;
			} else {
				bos = null;
			}

			File file = new File(new_path);
			if (file.exists()) {
				name = file.getName();
			}
		} catch (Exception e) {
			if (bis != null) {
				try {
					bis.close();
					bis = null;
				} catch (IOException e1) {
					bis = null;
				}
			} else {
				bis = null;
			}

			if (bos != null) {
				try {
					bos.close();
					bos = null;
				} catch (IOException e1) {
					bos = null;
				}
			} else {
				bos = null;
			}
			e.printStackTrace();
		}
		return name;
	}

	public boolean zipFile(String file, String temp_forder, String zipFileName) {
		boolean ok = false;
		FileInputStream in = null;
		FileOutputStream out = null;
		ZipOutputStream zipOut = null;
		try {
			in = new FileInputStream(file);
			out = new FileOutputStream(temp_forder + "/" + zipFileName); // 1
			zipOut = new ZipOutputStream(out);
			File file_link = new File(file);
			ZipEntry entry = new ZipEntry(file_link.getName());
			zipOut.putNextEntry(entry);
			int nNumber;
			byte[] buffer = new byte[512];
			while ((nNumber = in.read(buffer)) != -1)
				zipOut.write(buffer, 0, nNumber);
			zipOut.close();
			out.close();
			in.close();
			ok = true;
		} catch (Exception e) {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e1) {
				if (in != null) {
					in = null;
				}
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e1) {
				if (out != null) {
					out = null;
				}
			}
			try {
				if (zipOut != null) {
					zipOut.close();
				}
			} catch (Exception e1) {
				if (zipOut != null) {
					zipOut = null;
				}
			}
			e.printStackTrace();
		}
		return ok;
	}

	public boolean unZipFile(String file) {
		boolean ok = false;

		String unzipfile = file; // 瑙ｅ帇缂╃殑鏂囦欢鍚嶏紝鍖呭惈璺緞

		try {
			File olddirec = new File(unzipfile);
			ZipInputStream zin = new ZipInputStream(new FileInputStream(
					unzipfile));

			ZipEntry entry;

			while ((entry = zin.getNextEntry()) != null) {

				if (entry.isDirectory()) {
					File directory = new File(olddirec.getParent(),
							entry.getName());
					if (!directory.exists()) {
						if (!directory.mkdirs()) {
							System.exit(0);
						}
					}

					zin.closeEntry();
				}

				if (!entry.isDirectory()) {
					File myFile = new File(entry.getName());

					String ofile = file.replace(".zip", "");

					File fo = new File(ofile);

					if (!fo.exists()) {
						fo.mkdir();
					}

					FileOutputStream fout = new FileOutputStream(ofile + "/"
							+ myFile.getPath());

					DataOutputStream dout = new DataOutputStream(fout);
					byte[] b = new byte[1024];

					int len = 0;

					while ((len = zin.read(b)) != -1) {
						dout.write(b, 0, len);
					}

					dout.close();

					fout.close();

					zin.closeEntry();

					ok = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok;
	}

	public static String readSDFile(String fileName) {

		StringBuffer sb = new StringBuffer();
		String SDPATH = Environment.getExternalStorageDirectory().getPath();
		File file = new File(SDPATH + "//" + fileName);
    	try {
      		FileInputStream fis = new FileInputStream(file);
    		InputStreamReader read = new InputStreamReader (new FileInputStream(file),"UTF-8");	
			int c;
			while ((c = read.read()) != -1) {
				sb.append((char) c);
			}

			fis.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
    	
		return EncodingUtils.getString(sb.toString().getBytes(),"utf-8");

	}
}
