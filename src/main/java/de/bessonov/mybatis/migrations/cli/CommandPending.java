package de.bessonov.mybatis.migrations.cli;

import com.beust.jcommander.Parameters;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;


/**
 * MyBatis Migrations pending CLI Command
 *
 * @author Anton Bessonov
 */
@Parameters(
		separators = CommandBase.PARAMETERS_SEPARATOR,
		commandDescription = "Run pending migrations")
public class CommandPending extends CommandBase {

	public CommandPending(SpringMyBatisMigrations springMyBatisMigrations) {
		super(springMyBatisMigrations);
	}

	@Override
	public void operate() {
		getSpringMyBatisMigrations().pending();
	}
}
