package com.vela.iot.common;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.BitSet;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

public class YamlConf {
	private static YamlConf conf;
	public static BitSet initBits = new BitSet(12);
	private YamlConf() {
	}

	public static YamlConf getInstance() {
		if (conf == null) {
			conf = new YamlConf();
		}
		return conf;
	}

	public Map<String, Object> parse(String fileName)
			throws FileNotFoundException {
		Yaml yaml = new Yaml();
		Optional<String> oFileName = Optional.of(fileName);
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(oFileName.orElse("default.yml"));
		Map<String, Object> map = (Map<String, Object>) yaml
				.load(new InputStreamReader(is));
		return map;
	}
}
