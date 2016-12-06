package com.hundsun.hsccbp.nlp.extracts;

import static com.hundsun.hsccbp.nlp.extracts.ExtractUtil.mkdirs;
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingleExtractorTest {
	transient private SingleExtractor singleExtractor;
	transient final ExtractConfig extractConfig = new ExtractConfig();
	transient final private Path destPath = FileSystems.getDefault().getPath(
			"E:", "nlp", "shiyan", "sina", "mirror", "finance.sina.com.cn",
			"china", "20141117", "023120833619.prl");
	transient final Pattern pattern = Pattern
			.compile("publish\\_helper.*publish\\_helper\\_end");

	@Before
	public void initialize() {
		final Path filePath = FileSystems.getDefault().getPath(
				extractConfig.getRawPath());
		singleExtractor = new SingleExtractor(filePath, extractConfig);
	}

	@Test
	public void testExtract() throws IOException {
		mkdirs(singleExtractor.getFilePath());
		mkdirs(singleExtractor.getDestPath());
		final FileChannel readFileChannel = new FileInputStream(singleExtractor
				.getFilePath().toString()).getChannel();
		final FileChannel writeFileChannel = new FileOutputStream(
				singleExtractor.getDestPath().toString()).getChannel();
		singleExtractor.extract(readFileChannel, writeFileChannel);
		// ""表示没有匹配到字符串，所以下面的断言表示匹配到了字符串
		Assert.assertTrue(true);
	}

	@Test
	public void testextract() {
		final String srcStr = " <!--单图 end-->		<!-- publish_helper name='原始正文' --> 张三 <!-- publish_helper_end -->";
		final String destStr = singleExtractor.match(srcStr, pattern);
		assertEquals(
				"publish_helper name='原始正文' --> 张三 <!-- publish_helper_end",
				destStr);
	}

	@Test
	public void testExcludePatternStr() {
		String srcStr = "publish_helper name='原始正文' --> 张三 <!-- publish_helper_end";
		final String destStr = singleExtractor.excludePatternStr(srcStr,pattern);
		assertEquals("张三", destStr);
	}
	
	@Test
	public void testExcludeHtmlTag() {
		String srcStr = "<p> <strong>张三</strong></p>";
		final String destStr = singleExtractor.excludeHtmlTag(srcStr);
		assertEquals("张三", destStr);
	}
	
	@Test
	public void testReplaceBlackWithStop(){
		String srcStr = "正式。     我是兵  我是匪 你好啊。你来自哪里　　";
		final String destStr = singleExtractor.replaceBlackWithStop(srcStr);
		assertEquals("正式。我是兵。我是匪。你好啊。你来自哪里。", destStr);
	}

	@Test
	public void testSingleExtractor() {
		assertEquals(extractConfig.getRuleMap().get("sina.com.cn").toString(),
				singleExtractor.getRule().toString());
		assertEquals(destPath.toString(), singleExtractor.getDestPath()
				.toString());
	}

}
