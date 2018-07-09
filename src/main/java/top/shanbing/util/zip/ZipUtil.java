package top.shanbing.util.zip;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 压缩工具类
 * 
 * 功能：传入字节码，返回 压缩/解压缩 之后的字节码
 * 
 * 使用场景：数据存入redis时，压缩存进去，取出来时进行解压，加大同内存下单机能存放的数据量
 * 
 * 后期可以与redis整合
 * 
 *
 * @author KangKai
 * @date 2017年3月28日
 */
public final class ZipUtil {

	private static final ThreadLocal <Deflater> THREAD_LOCAL_ZIP = new ThreadLocal <Deflater>() {
		protected synchronized Deflater initialValue() {
			return new Deflater(Deflater.BEST_SPEED);
		}
	};
	private static final ThreadLocal <Inflater> THREAD_LOCAL_UNZIP = new ThreadLocal <Inflater>() {
		protected synchronized Inflater initialValue() {
			return new Inflater();
		}
	};
	/**
	 * 压缩时，结果缓冲区大小为输入的?分之一
	 **/
	private static final int ZIP_BUFFER_RATIO = 3;
	/**
	 * 解压时，结果缓冲区大小为输入的?倍
	 **/
	private static final int UNZIP_BUFFER_RATIO = 5;
	/**
	 * 缓冲区大小为?字节
	 **/
	private static final int BUFFER_SIZE = 1024 * 4;

	private ZipUtil() {

	}

	/**
	 * 压缩数据
	 *
	 * @param data
	 * @return
	 */
	public static byte[] zip(byte[] data) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length / ZIP_BUFFER_RATIO);
		Deflater zip = THREAD_LOCAL_ZIP.get();
		zip.setInput(data);
		zip.finish();
		int count;
		byte[] buffer = new byte[BUFFER_SIZE];
		do {
			count = zip.deflate(buffer);
			baos.write(buffer, 0, count);
		} while (!zip.finished());
		zip.reset();
		return baos.toByteArray();
	}

	/**
	 * 解压数据
	 *
	 * @param data
	 * @return
	 */
	public static byte[] unzip(byte[] data) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length * UNZIP_BUFFER_RATIO);
		Inflater unzip = THREAD_LOCAL_UNZIP.get();
		unzip.setInput(data);
		int count;
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			do {
				count = unzip.inflate(buffer);
				baos.write(buffer, 0, count);
			} while (!unzip.finished());
		} catch (DataFormatException e) {
			throw new RuntimeException(e);
		} finally {
			unzip.reset();
		}

		return baos.toByteArray();
	}

}
