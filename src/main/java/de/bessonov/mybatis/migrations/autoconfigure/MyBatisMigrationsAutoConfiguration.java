package de.bessonov.mybatis.migrations.autoconfigure;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.migration.operations.DatabaseOperation;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import de.bessonov.mybatis.migrations.SpringMyBatisMigrations;
import de.bessonov.mybatis.migrations.cli.MyBatisMigrationsCliHandler;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for mybatis database migrations.
 *
 * @author Anton Bessonov
 */
@Configuration
@ConditionalOnClass(DatabaseOperation.class)
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty(prefix = "mybatis.migrations", name = "enabled", matchIfMissing = true)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class MyBatisMigrationsAutoConfiguration {

	private static final String MY_BATIS_MIGRATIONS_CLI_HANDLER_BEAN = "myBatisMigrationsCliHandler";

	@Configuration
	@ConditionalOnMissingBean(SpringMyBatisMigrations.class)
	@EnableConfigurationProperties(MyBatisMigrationsProperties.class)
	@Import(MyBatisMigrationsJpaDependencyConfiguration.class)
	public static class MyBatisMigrationsConfiguration {

		private final MyBatisMigrationsProperties properties;

		private final DataSource dataSource;

		private final DataSource myBatisMigrationsDataSource;

		private ResourcePatternResolver resourceResolver;

		public MyBatisMigrationsConfiguration(MyBatisMigrationsProperties properties,
				ObjectProvider<DataSource> dataSource,
				@MyBatisMigrationsDataSource ObjectProvider<DataSource> myBatisMigrationsDataSource,
				ResourcePatternResolver resourceResolver) {

			this.properties = properties;
			this.resourceResolver = resourceResolver;
			this.dataSource = dataSource.getIfUnique();
			this.myBatisMigrationsDataSource = myBatisMigrationsDataSource.getIfAvailable();
		}

		private DataSource getDataSource() {
			if (this.myBatisMigrationsDataSource != null) {
				return this.myBatisMigrationsDataSource;
			}

			if (this.properties.getUrl() == null) {
				return this.dataSource;
			}

			return DataSourceBuilder.create().url(this.properties.getUrl())
					.username(this.properties.getUser())
					.password(this.properties.getPassword()).build();
		}

		@Bean
		@ConditionalOnMissingBean
		@ConfigurationProperties(prefix = "mybatis.migrations")
		public SpringMyBatisMigrations springMyBatisMigrations() {
			SpringMyBatisMigrations springMyBatisMigrations = new SpringMyBatisMigrations(
					this.resourceResolver,
					getDataSource(),
					this.properties);
			return springMyBatisMigrations;
		}

		/**
		 * CLI handler bean
		 * @param springApplicationArguments for CLI args
		 * @return constructed MyBatisMigrationsCliHandler bean
		 */
		@Bean(name = MY_BATIS_MIGRATIONS_CLI_HANDLER_BEAN)
		@ConditionalOnMissingBean
		public MyBatisMigrationsCliHandler myBatisMigrationsCliHandler(
				ApplicationArguments springApplicationArguments) {
			return new MyBatisMigrationsCliHandler(springApplicationArguments.getSourceArgs());
		}
	}

	/**
	 * Additional configuration to ensure that {@link EntityManagerFactory} beans
	 * depend-on the mybatis migrations bean.
	 */
	@Configuration
	@ConditionalOnClass(LocalContainerEntityManagerFactoryBean.class)
	@ConditionalOnBean(AbstractEntityManagerFactoryBean.class)
	protected static class MyBatisMigrationsJpaDependencyConfiguration
			extends EntityManagerFactoryDependsOnPostProcessor {

		public MyBatisMigrationsJpaDependencyConfiguration() {
			super(MY_BATIS_MIGRATIONS_CLI_HANDLER_BEAN);
		}
	}

}
