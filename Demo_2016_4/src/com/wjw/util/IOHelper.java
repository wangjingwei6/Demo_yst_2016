package com.wjw.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class IOHelper {

	public static String readTextFile(String filePath) {
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(filePath));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = bReader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] readBinaryFile(String filePath) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;

		try {
			fis = new FileInputStream(filePath);
			baos = new ByteArrayOutputStream();
			int c = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((c = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, c);
				baos.flush();
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static boolean writeTextFile(String content, String fileName) {
		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter(new FileWriter(fileName, true), true);
			pWriter.write(content);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pWriter.close();
		}
		return false;
	}

	public static boolean writeTextFile2(String content, String fileName) {
		BufferedWriter bWriter = null;
		try {
			bWriter = new BufferedWriter(new FileWriter(fileName, true));
			bWriter.write(content);
			bWriter.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean writeBinaryFile(byte[] data, String filePath) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			bos.write(data, 0, data.length);
			bos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean copyTextFile(String filePath, String destFilePath) {
		BufferedReader bReader = null;
		BufferedWriter bWriter = null;
		try {
			bReader = new BufferedReader(new FileReader(filePath));
			bWriter = new BufferedWriter(new FileWriter(destFilePath, true));

			String line = "";
			while ((line = bReader.readLine()) != null) {
				bWriter.write(line);
				bWriter.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bReader.close();
				bWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean inputStream2File(InputStream is, String destFilePath) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File file = new File(destFilePath);
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(new FileOutputStream(file));
			byte b[] = new byte[8 * 1024];
			int len = 0;
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
				bos.flush();
			}
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean copyBinaryFile(String filePath, String destFilePath) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(new FileOutputStream(destFilePath));

			int c = 0;
			byte[] buffer = new byte[8 * 1024];
			while ((c = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, c);
				bos.flush();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		return file.delete();
	}

	public static boolean isExistFile(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static String getFileExtension(String filePath) {
		return filePath.substring(filePath.lastIndexOf("."), filePath.length());
	}

	public static byte[] streamToByte(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int c = 0;
		byte[] buffer = new byte[8 * 1024];
		try {
			while ((c = is.read(buffer)) != -1) {
				baos.write(buffer, 0, c);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	public static String streamToString(InputStream is, String charsetName) {
		BufferedInputStream bis = new BufferedInputStream(is);
		StringBuilder sb = new StringBuilder();
		int c = 0;
		byte[] buffer = new byte[8 * 1024];
		try {
			while ((c = bis.read(buffer)) != -1) {
				sb.append(new String(buffer, charsetName));
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static InputStream stringToInputStream(String str) {
		// InputStream in_nocode = new ByteArrayInputStream(str.getBytes());
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return is;
	}

}
