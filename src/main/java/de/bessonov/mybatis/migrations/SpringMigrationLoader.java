package de.bessonov.mybatis.migrations;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.ibatis.migration.Change;
import org.apache.ibatis.migration.MigrationException;
import org.apache.ibatis.migration.MigrationLoader;
import org.apache.ibatis.migration.MigrationReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Spring migrations loader
 *
 * @author Anton Bessonov
 */
public class SpringMigrationLoader implements MigrationLoader {

	protected static final String BOOTSTRAP_SQL = "bootstrap.sql";

	protected static final String ONABORT_SQL = "onabort.sql";

	private ResourcePatternResolver resourcePatternResolver;
	private String path;
	private String charset;
	private Properties properties;

	public SpringMigrationLoader(
			ResourcePatternResolver resourcePatternResolver,
			String path,
			String charset,
			Properties properties) {
		this.resourcePatternResolver = resourcePatternResolver;
		this.path = path;
		this.charset = charset;
		this.properties = properties;
	}

	@Override
	public List<Change> getMigrations() {

		Collection<String> filenames = new TreeSet<>();

		for (Resource res : getResources("/*.sql")) {
			filenames.add(res.getFilename());
		}

		filenames.remove(BOOTSTRAP_SQL);
		filenames.remove(ONABORT_SQL);

		return filenames.stream()
			.map(this::parseChangeFromFilename)
			.collect(Collectors.toList());
	}

	@Override
	public Reader getScriptReader(Change change, boolean undo) {
		return getReader(change.getFilename(), undo);
	}

	@Override
	public Reader getBootstrapReader() {
		return getReader(BOOTSTRAP_SQL, false);
	}

	@Override
	public Reader getOnAbortReader() {
		return getReader(ONABORT_SQL, false);
	}

	protected Resource getResource(String pattern) {
		return this.resourcePatternResolver.getResource(this.path + "/" + pattern);
	}

	protected Resource[] getResources(String pattern) {
		try {
			return this.resourcePatternResolver.getResources(this.path + pattern);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected Change parseChangeFromFilename(String filename) {
		try {
			String name = filename.substring(0, filename.lastIndexOf("."));

			int separator = name.indexOf("_");

			BigDecimal id = new BigDecimal(name.substring(0, separator));

			String description = name.substring(separator + 1).replace('_', ' ');

			Change change = new Change(id);
			change.setFilename(filename);
			change.setDescription(description);

			return change;

		} catch (Exception e) {
			throw new MigrationException("Error parsing change from file.  Cause: " + e, e);
		}
	}

	protected Reader getReader(String fileName, boolean undo) {
		try {
			Resource scriptResource = getResource(fileName);
			if (scriptResource.exists()) {
				return new MigrationReader(scriptResource.getInputStream(), charset, undo, properties);
			}
			return null;
		} catch (IOException e) {
			throw new MigrationException("Error reading " + fileName, e);
		}
	}
}
