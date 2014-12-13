package com.hundsun.hsccbp.nlp.extracts;

import java.nio.file.FileSystems;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.hundsun.hsccbp.nlp.tagger.MultiFileNlpChain;
import com.hundsun.hsccbp.nlp.tagger.SingleFileNlpChain;
import com.hundsun.hsccbp.nlp.tagger.NlpWrap;

@Path("/nlp")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class ExtractResource {
	transient final private ExtractConfig extractConfig;

	public ExtractResource(final ExtractConfig extractConfig) {
		this.extractConfig = extractConfig;
	}

	@Path("/singleExtract")
	@GET
	@Timed
	public ExtractResult singleExtract(
			@QueryParam("path") final Optional<String> path) {
		final java.nio.file.Path filePath = FileSystems.getDefault().getPath(
				path.or(extractConfig.getRawPath()));
		final SingleExtractor extractor = new SingleExtractor(filePath,
				extractConfig);
		return extractor.extract();
	}

	@Path("/complexExtract")
	@GET
	@Timed
	public ExtractResult complexExtract(
			@QueryParam("path") final Optional<String> path) {
		final java.nio.file.Path filePath = FileSystems.getDefault().getPath(
				path.or(extractConfig.getRawPath()));
		final ComplexExtractor extractor = new ComplexExtractor(filePath,
				extractConfig);
		return extractor.extract();
	}

	@Path("/singlePosTag")
	@GET
	@Timed
	public ExtractResult singlePosTag(
			@QueryParam("path") final Optional<String> path,
			@QueryParam("modelFilePath") final Optional<String> modelFilePath) {
		final java.nio.file.Path filePath = FileSystems.getDefault().getPath(
				path.or(extractConfig.getRawPath()));
		NlpWrap posTagger = new SingleFileNlpChain(filePath, extractConfig,
				modelFilePath.or(extractConfig.getModelFilePath()));
		return posTagger.nlp();
	}

	@Path("/complexPosTag")
	@GET
	@Timed
	public ExtractResult complexPosTag(
			@QueryParam("path") final Optional<String> path,
			@QueryParam("modelFilePath") final Optional<String> modelFilePath) {
		final java.nio.file.Path filePath = FileSystems.getDefault().getPath(
				path.or(extractConfig.getRawPath()));
		NlpWrap posTagger = new MultiFileNlpChain(filePath, extractConfig,
				modelFilePath.or(extractConfig.getModelFilePath()));
		return posTagger.nlp();
	}
}
