package com.hundsun.hsccbp.nlp.extracts;

import static com.hundsun.hsccbp.nlp.extracts.ExtractUtil.mkdirs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fnlp.nlp.cn.Chars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单个文件抽取器，把原始文件按一定的规则，抽取成目标文件。
 * 
 * @author pengqb
 */
public class SingleExtractor extends AbstractExtractor {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SingleExtractor.class);
	/**
	 * 文件抽取规则
	 */
	private Pattern rule;

	/**
	 * 抽取后生成的目标文件路径
	 */
	private Path destPath;

	public SingleExtractor(final Path filePath,
			final ExtractConfig extractConfig) {
		super(filePath, extractConfig);
		attachRuleToFile();
		genDestFilePath();
	}

	public Pattern getRule() {
		return rule;
	}

	public void setRule(final Pattern rule) {
		this.rule = rule;
	}

	public Path getDestPath() {
		return destPath;
	}

	public void setDestPath(final Path destPath) {
		this.destPath = destPath;
	}

	@Override
	public ExtractResult extract() {
		String code = CExtract.SUCCESS_CODE;
		String msg = "";
		try {
			mkdirs(this.filePath);
			mkdirs(this.destPath);
			final FileChannel readFileChannel = new FileInputStream(
					this.filePath.toString()).getChannel();
			final FileChannel writeFileChannel = new FileOutputStream(
					this.destPath.toString()).getChannel();
			extract(readFileChannel, writeFileChannel);
		} catch (Exception e) {
			code = CExtract.FAIL_CODE;
			msg = "执行文件抽取的过程中发生错误，请联系系统管理员";
			LOGGER.error(msg, e);
		}
		return new ExtractResult(code, msg);
	}

	public void extract(final FileChannel readFileChannel,
			final FileChannel writeFileChannel) throws IOException {
		final CharSequence content = readChannel(readFileChannel);
		// TODO 几个字符串的命名不好
		final String matchStr = match(content);
		final String dbcStr =Chars.ToDBC(matchStr);//全角字符转换成半角字符
		final String excludedPatternStr = excludePatternStr(dbcStr);
		final String excludedHtmlStr = excludeHtmlTag(excludedPatternStr);
		final String excludeBlankStr = replaceBlackWithStop(excludedHtmlStr);
		if ("".equals(matchStr)) {
			LOGGER.info("被抽取文件无法抽取出内容，被抽取文件名={},抽取规则={}",
					this.filePath.toString(), this.rule.toString());
		} else {
			// 从新浪下载的文件格式是gbk编码，nlp加工的是utf-8编码，所以要进行编码转换
			String matchStrUtf8 = ExtractUtil.changeCharset(excludeBlankStr,
					CExtract.FILE_CHARSET_GBK, CExtract.FILE_CHARSET_UTF8);
			// 保存成utf-8编码格式，因为nlp加工的是utf-8编码格式的文档。
			writeChannel(writeFileChannel, matchStrUtf8);
		}
	}

	/**
	 * 从通道里读出文件内容
	 * 
	 * @param fc
	 * @return
	 * @throws IOException
	 */
	private CharSequence readChannel(final FileChannel fileChannel)
			throws IOException {
		final ByteBuffer bbuf = fileChannel.map(FileChannel.MapMode.READ_ONLY,
				0, (int) fileChannel.size());
		final CharBuffer cbuf = Charset.forName(CExtract.FILE_CHARSET_GBK)
				.newDecoder().decode(bbuf);
		fileChannel.close();
		return cbuf;
	}

	/**
	 * 把字符串写入通道
	 * 
	 * @param fileChannel
	 * @param charSequence
	 * @throws IOException
	 */
	private void writeChannel(final FileChannel fileChannel,
			final CharSequence charSequence) throws IOException {
		final CharBuffer charBuffer = CharBuffer.wrap(charSequence);
		byte[] byteSequence = Charset.forName(CExtract.FILE_CHARSET_UTF8)
				.newEncoder().encode(charBuffer).array();
		// TODO
		// byteArray.length不对，总是包含了空格的字符，所以输出的文件包含了很多空格，现自己写了一个方法获得不包含空格的长度，不知道jdk本身是怎么解决的。
		// final ByteBuffer bbuf =
		// ByteBuffer.wrap(byteArray,0,byteArray.length);
		Integer length = getRealByteSequenceLength(byteSequence);
		final ByteBuffer bbuf = ByteBuffer.wrap(byteSequence, 0, length);
		fileChannel.write(bbuf);
		fileChannel.close();
	}

	/**
	 * 得到字节序列的真实长度（除去后面的空格），
	 * 
	 * @param byteSequence
	 * @return
	 */
	private Integer getRealByteSequenceLength(byte[] byteSequence) {
		int length = byteSequence.length;
		for (int i = byteSequence.length; i > 0; i--) {
			if (byteSequence[i - 1] != 0) {
				length = i;
				break;
			}
		}
		return length;
	}

	/**
	 * 按正则表达式pattern，匹配字符串srcStr，如果匹配成功，返回被匹配的字符串。
	 * 
	 * @param srcStr
	 * @param pattern
	 * @return
	 */
	public String match(final CharSequence srcStr) {
		return match(srcStr, this.rule);
	}

	String match(final CharSequence srcStr, final Pattern pattern) {
		final Matcher matcher = pattern.matcher(srcStr);
		String matchStr = "";
		while (matcher.find()) {
			matchStr += matcher.group();
		}
		return matchStr.trim();
	}

	/**
	 * 替换正则表达式边界部分为空格字符
	 * 
	 * @param srcStr
	 * @param pattern
	 * @return
	 */
	public String excludePatternStr(final String srcStr) {
		return excludePatternStr(srcStr, this.rule);
	}

	String excludePatternStr(final String srcStr, final Pattern pattern) {
		String regexs[] = pattern.toString().split("\\.\\*");
		String remainStr = srcStr;
		if (null != regexs[0]) {
			remainStr = remainStr.replaceFirst(regexs[0] + ".*\\-\\-\\>", "");
		}
		if (null != regexs[1]) {
			remainStr = remainStr
					.replaceFirst("\\<\\!\\-\\-.*" + regexs[1], "");
		}
		return remainStr.trim();
	}

	/**
	 * 替换html标签为空格字符
	 * 
	 * @param srcStr
	 * @return
	 */
	public String excludeHtmlTag(final String srcStr) {
		String remainStr = srcStr;
		// remainStr =
		// remainStr.replaceAll("(\\<(\\/)?+(\\w|\\s|\\p{Punct}).*\\>){1}", "");
		remainStr = remainStr.replaceAll(
				"(\\<(\\/)?+(\\w|\\s|\\p{Punct})*\\>)", "");
		return remainStr.trim();
	}
	
	/**
	 * 把空格字符串替换成句号
	 * @param srcStr
	 * @return
	 */
	public String replaceBlackWithStop(final String srcStr){
		String remainStr = srcStr;
		remainStr = remainStr.replaceAll(
				"。*(\\s+)", "。");
		return remainStr.trim();
	}

	/**
	 * 根据被抽取文件所在路径特点， 附加一个抽取规则给该文件。 <br>
	 */
	private void attachRuleToFile() {
		this.rule = Pattern.compile(".");
		for (Entry<String, Pattern> entry : extractConfig.getRuleMap()
				.entrySet()) {
			if (filePath.toString().contains(entry.getKey())) {
				this.rule = entry.getValue();
				break;
			}
		}
	}

	/**
	 * 根据被抽取文件所在路径特点，生产目标文件路径。
	 */
	private void genDestFilePath() {
		final String destStr = filePath
				.toString()
				.replaceFirst("shtml|html|htm", CExtract.EXTRACTED_FILE_EXTEND)
				.replace(extractConfig.getRawHome(),
						extractConfig.getExtractedHome());
		this.destPath = FileSystems.getDefault().getPath(destStr);
	}
}
