package com.vela.iot.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

public class YamlConf {
	private static YamlConf conf;

	private YamlConf() {
	}

	public YamlConf getInstance() {
		if (conf == null) {
			conf = new YamlConf();
		}
		return conf;
	}

	public Map<String, Object> parse(Optional<String> fileName)
			throws FileNotFoundException {
		Yaml yaml = new Yaml();
		URL url = YamlConf.class.getClassLoader().getResource(
				fileName.orElse("default.yaml"));
		Map<String, Object> map = (Map<String, Object>) yaml
				.load(new FileInputStream(url.getFile()));
		return map;
	}
}
