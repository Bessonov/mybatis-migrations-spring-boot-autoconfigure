package de.bessonov.mybatis.migrations.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;


/**
 * MyBatis Migrations up CLI Command
 *
 * @author Anton Bessonov
 */
@Parameters(
		separators = CommandBase.PARAMETERS_SEPARATOR,
		commandDescription = "Run unapplied migrations")
public class CommandUp extends CommandBase {

	@Parameter(names = "--steps", description = "Specifies how many steps should be executed. All if not set.")
	private Integer steps;

	public CommandUp(SpringMyBatisMigrations springMyBatisMigrations) {
		super(springMyBatisMigrations);
	}

	@Override
	public void operate() {
		getSpringMyBatisMigrations().up(this.steps);
	}
}
