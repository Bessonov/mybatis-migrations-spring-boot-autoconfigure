package de.bessonov.mybatis.migrations;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.apache.ibatis.migration.DataSourceConnectionProvider;
import org.apache.ibatis.migration.operations.BootstrapOperation;
import org.apache.ibatis.migration.operations.DownOperation;
import org.apache.ibatis.migration.operations.PendingOperation;
import org.apache.ibatis.migration.operations.StatusOperation;
import org.apache.ibatis.migration.operations.UpOperation;
import org.apache.ibatis.migration.operations.VersionOperation;
import org.springframework.core.io.support.ResourcePatternResolver;

import de.bessonov.mybatis.migrations.autoconfigure.MyBatisMigrationsProperties;

/**
 * Helper class to run migration commands
 *
 * @author Anton Bessonov
 */
public class SpringMyBatisMigrations {

	private DataSource dataSource;
	private MyBatisMigrationsProperties properties;
	private SpringMigrationLoader springMigrationLoader;

	public SpringMyBatisMigrations(
			ResourcePatternResolver resourcePatternResolver,
			DataSource dataSource,
			MyBatisMigrationsProperties properties) {
		this.dataSource = dataSource;
		this.properties = properties;
		this.springMigrationLoader = new SpringMigrationLoader(
				resourcePatternResolver,
				properties.getPath(),
				properties.getCharset(),
				properties.getProperties());
	}

	public void status() {
		operate(new StatusOperation()::operate);
	}

	public void bootstrap(boolean force) {
		operate(new BootstrapOperation(force)::operate);
	}

	public void up(Integer steps) {
		operate(new UpOperation(steps)::operate);
	}

	public void version(BigDecimal version) {
		operate(new VersionOperation(version)::operate);
	}

	public void pending() {
		operate(new PendingOperation()::operate);
	}

	public void down(Integer steps) {
		operate(new DownOperation(steps == null ? 1 : steps)::operate);
	}

	public void operate(Operator operator) {
		operator.operate(
				new DataSourceConnectionProvider(this.dataSource),
				this.springMigrationLoader,
				this.properties,
				System.out);
	}

}
