package de.bessonov.mybatis.migrations;

import java.io.PrintStream;

import org.apache.ibatis.migration.ConnectionProvider;
import org.apache.ibatis.migration.MigrationLoader;
import org.apache.ibatis.migration.options.DatabaseOperationOption;

public interface Operator {
	public Object operate(
			ConnectionProvider connectionProvider,
			MigrationLoader migrationsLoader,
			DatabaseOperationOption option,
			PrintStream printStream);
}
