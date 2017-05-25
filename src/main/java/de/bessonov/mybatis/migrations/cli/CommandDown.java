package de.bessonov.mybatis.migrations.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;

/**
 * MyBatis Migrations down CLI Command
 *
 * @author Anton Bessonov
 */
@Parameters(
		separators = CommandBase.PARAMETERS_SEPARATOR,
		commandDescription = "Runs the undo section of migration scripts")
public class CommandDown extends CommandBase {

	@Parameter(names = "--steps", description = "Specifies how many steps should be executed. One if not set.")
	private Integer steps;

	public CommandDown(SpringMyBatisMigrations springMyBatisMigrations) {
		super(springMyBatisMigrations);
	}

	@Override
	public void operate() {
		getSpringMyBatisMigrations().down(this.steps);
	}
}
