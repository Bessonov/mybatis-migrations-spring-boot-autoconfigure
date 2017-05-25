package de.bessonov.mybatis.migrations.cli;

import java.math.BigDecimal;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;

/**
 * MyBatis Migrations version CLI Command
 *
 * @author Anton Bessonov
 */
@Parameters(
		separators = CommandBase.PARAMETERS_SEPARATOR,
		commandDescription = "Migrate to specific version")
public class CommandVersion extends CommandBase {

	@Parameter(
			names = "--version",
			required = true,
			description = "Specifies version to migrate")
	private BigDecimal version;

	public CommandVersion(SpringMyBatisMigrations springMyBatisMigrations) {
		super(springMyBatisMigrations);
	}

	@Override
	public void operate() {
		getSpringMyBatisMigrations().version(this.version);
	}
}
