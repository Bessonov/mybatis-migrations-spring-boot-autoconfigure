package de.bessonov.mybatis.migrations.cli;

import com.beust.jcommander.Parameters;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;


/**
 * MyBatis Migrations status CLI Command
 *
 * @author Anton Bessonov
 */
@Parameters(
		separators = CommandBase.PARAMETERS_SEPARATOR,
		commandDescription = "Report the current state of the database")
public class CommandStatus extends CommandBase {

	public CommandStatus(SpringMyBatisMigrations springMyBatisMigrations) {
		super(springMyBatisMigrations);
	}

	@Override
	public void operate() {
		getSpringMyBatisMigrations().status();
	}
}
