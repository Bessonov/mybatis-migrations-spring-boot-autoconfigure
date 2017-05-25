package de.bessonov.mybatis.migrations.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Qualifier annotation for a DataSource to be injected in to MyBatis Migrations. If used
 * for a second data source, the other (main) one would normally be marked as {@code @Primary}.
 *
 * @author Anton Bessonov
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE,
	ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface MyBatisMigrationsDataSource {

}
