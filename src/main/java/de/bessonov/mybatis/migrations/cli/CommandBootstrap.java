package de.bessonov.mybatis.migrations.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;

/**
 * MyBatis Migrations bootstrap CLI Command
 *
 * @author Anton Bessonov
 */
@Parameters(
		separators = CommandBase.PARAMETERS_SEPARATOR,
		commandDescription = "Runs the bootstrap migration script")
public class CommandBootstrap extends CommandBase {

	@Parameter(names = "--force", description = "Force execution")
	private boolean force;

	public CommandBootstrap(SpringMyBatisMigrations springMyBatisMigrations) {
		super(springMyBatisMigrations);
	}

	@Override
	public void operate() {
		getSpringMyBatisMigrations().bootstrap(this.force);
	}
}
