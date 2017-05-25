package de.bessonov.mybatis.migrations.cli;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;

/**
 * Base class for commands
 *
 * @author Anton Bessonov
 */
public abstract class CommandBase {

	protected static final String PARAMETERS_SEPARATOR = "=";

	private SpringMyBatisMigrations springMyBatisMigrations;

	public CommandBase(SpringMyBatisMigrations springMyBatisMigrations) {
		this.springMyBatisMigrations = springMyBatisMigrations;
	}

	public SpringMyBatisMigrations getSpringMyBatisMigrations() {
		return springMyBatisMigrations;
	}

	abstract void operate();
}
