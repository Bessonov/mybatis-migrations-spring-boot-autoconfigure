package de.bessonov.mybatis.migrations.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;


/**
 * Main CLI Command
 *
 * @author Anton Bessonov
 */
@Parameters(commandDescription = "MyBatis Migrations CLI")
public class CommandMain {

	@Parameter(names = "--help", help = true, description = "Shows usage")
	private boolean help;

	@Parameter(names = "--no-exit", description = "Don't exit after command execution")
	private boolean noExit = false;

	public boolean isHelp() {
		return help;
	}

	public boolean isNoExit() {
		return noExit;
	}

	public boolean isExit() {
		return noExit == false;
	}
}
