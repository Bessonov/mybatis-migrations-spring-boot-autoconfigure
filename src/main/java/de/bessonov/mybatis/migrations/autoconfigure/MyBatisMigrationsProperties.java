package de.bessonov.mybatis.migrations.autoconfigure;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.ibatis.migration.options.DatabaseOperationOption;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties to configure MyBatis Migrations
 *
 * @see <a href="https://github.com/mybatis/migrations/blob/master/src/main/java/org/apache/ibatis/migration/template_environment.properties">
 * 	template_environment.properties</a>
 * @see DatabaseOperationOption
 *
 * @author Anton Bessonov
 */
@ConfigurationProperties(prefix = "mybatis.migrations", ignoreUnknownFields = false)
public class MyBatisMigrationsProperties extends DatabaseOperationOption {

	/**
	 * Enable MyBatis Migrations
	 */
	private boolean enabled = true;

	/**
	 * Path to migration scripts
	 */
	private String path = "classpath:scripts";

	/**
	 * The character set that scripts are encoded with
	 */
	private String charset = "UTF-8";

	/**
	 * Login user of the database to migrate.
	 */
	private String user;

	/**
	 * Login password of the database to migrate.
	 */
	private String password;

	/**
	 * JDBC url of the database to migrate. If not set, the primary configured data source
	 * is used.
	 */
	private String url;

	/**
	 * Variable substitutions in the form of ${variable} in the migration scripts
	 */
	private Properties properties = new Properties();

	/**
	 * Name of the CLI command
	 */
	private String cliCommand = "migrations";

	/**
	 * Default command which is executed, if migration
	 * is enabled and no CLI command is provided
	 */
	private Collection<String> migrationCommand = new ArrayList<>(asList("--no-exit", "up"));

	@PostConstruct
	public void initializeProperties() {
		// add default property
		properties.setProperty("changelog",
				properties.getProperty("changelog", getChangelogTable()));
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCliCommand() {
		return cliCommand;
	}

	public void setCliCommand(String cliCommand) {
		this.cliCommand = cliCommand;
	}

	public Collection<String> getMigrationCommand() {
		return migrationCommand;
	}

	public void setMigrationCommand(Collection<String> migrationCommand) {
		this.migrationCommand = migrationCommand;
	}
}
