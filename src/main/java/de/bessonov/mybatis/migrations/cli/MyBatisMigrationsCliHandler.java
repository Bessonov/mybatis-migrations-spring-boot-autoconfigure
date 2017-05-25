package de.bessonov.mybatis.migrations.cli;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.beust.jcommander.JCommander;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;
import de.bessonov.mybatis.migrations.autoconfigure.MyBatisMigrationsProperties;

/**
 * CLI handler
 *
 * @author Anton Bessonov
 */
public class MyBatisMigrationsCliHandler implements InitializingBean {

	@Autowired
	private SpringMyBatisMigrations springMyBatisMigrations;

	@Autowired
	private MyBatisMigrationsProperties myBatisMigrationsProperties;

	private String[] args;

	public MyBatisMigrationsCliHandler(String... args) {
		this.args = args;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String[] commandArgs = null;

		// check cli
		int commandPosition = Arrays.asList(args).indexOf(myBatisMigrationsProperties.getCliCommand());

		// found migration cli command?
		if (commandPosition >= 0) {
			// remove migration command from args
			commandArgs = Arrays.copyOfRange(args, commandPosition + 1, args.length);

			// show usage if migration called without arguments
			if (commandArgs.length == 0) {
				commandArgs = new String[]{"--help"};
			}
		}

		// check auto migration if no cli called
		if (commandArgs == null) {
			Collection<String> migrationCommand = myBatisMigrationsProperties.getMigrationCommand();
			commandArgs = migrationCommand.toArray(new String[migrationCommand.size()]);
		}

		if (commandArgs != null && commandArgs.length > 0) {
			Map<String, CommandBase> commands = new LinkedHashMap<>();

			commands.put("status", new CommandStatus(springMyBatisMigrations));
			commands.put("bootstrap", new CommandBootstrap(springMyBatisMigrations));
			commands.put("up", new CommandUp(springMyBatisMigrations));
			commands.put("pending", new CommandPending(springMyBatisMigrations));
			commands.put("version", new CommandVersion(springMyBatisMigrations));
			commands.put("down", new CommandDown(springMyBatisMigrations));

			CommandMain commandMain = new CommandMain();
			JCommander jCommander = new JCommander(commandMain);

			for (Entry<String, CommandBase> command : commands.entrySet()) {
				jCommander.addCommand(command.getKey(), command.getValue());
			}

			jCommander.parse(commandArgs);

			if (commandMain.isHelp()) {
				jCommander.usage();
				if (commandMain.isExit()) {
					exit();
				}
			}

			// action!
			commands.get(jCommander.getParsedCommand()).operate();

			if (commandMain.isExit()) {
				exit();
			}
		}
	}

	private void exit() {
		System.exit(0);
	}

}
