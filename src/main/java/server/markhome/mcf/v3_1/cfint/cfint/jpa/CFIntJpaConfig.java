// Description: Java 25 JPA implementation of a CFInt connection configuration

/*
 *	server.markhome.mcf.CFInt
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFInt - Internet Essentials
 *	
 *	This file is part of Mark's Code Fractal CFInt.
 *	
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *	
 *	http://www.apache.org/licenses/LICENSE-2.0
 *	
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *	
 */

package server.markhome.mcf.v3_1.cfint.cfint.jpa;
//package server.markhome.mcf.v3_1.cfint.cfint.jpa;

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cflib.xml.CFLibXmlUtil;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.jpa.*;

@Configuration
@EntityScan(basePackages = "server.markhome.mcf.v3_1.cfint.cfint.jpa")
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "server.markhome.mcf.v3_1.cfint.cfint.jpa",
    entityManagerFactoryRef = "cfint31EntityManagerFactory",
    transactionManagerRef = "cfint31TransactionManager"
)
public class CFIntJpaConfig
{
	@Autowired
	@Qualifier("appMergedProperties")
	private Properties appMergedProperties;

    public final static String persistenceUnitName = "cfint31PU";

    private static final AtomicReference<DataSource> refCFInt31DataSource = new AtomicReference<>(null);
    private static final AtomicReference<Properties> cfint31JpaProperties = new AtomicReference<>(null);
    private static final AtomicReference<LocalContainerEntityManagerFactoryBean> refCFInt31EntityManagerFactoryBean = new AtomicReference<>(null);

    @Bean(name = "cfint31DataSource")
    @Primary
    public DataSource cfint31DataSource() {
        if (refCFInt31DataSource.get() == null) {
            Properties props = appMergedProperties;

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("cfint31.jakarta.persistence.jdbc.driver", props.getProperty("jakarta.persistence.jdbc.driver", "org.postgresql.Driver")));
            config.setJdbcUrl(props.getProperty("cfint31.jakarta.persistence.jdbc.url", props.getProperty("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/yourdb")));
            config.setUsername(props.getProperty("cfint31.jakarta.persistence.jdbc.user", props.getProperty("jakarta.persistence.jdbc.user", "postgres")));
            config.setPassword(props.getProperty("cfint31.jakarta.persistence.jdbc.password", props.getProperty("jakarta.persistence.jdbc.password", "pgpassword")));

            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("cfint31.hikari.maximumPoolSize", props.getProperty("hikari.maximumPoolSize", "10"))));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("cfint31.hikari.minimumIdle", props.getProperty("hikari.minimumIdle", "5"))));
            config.setPoolName(props.getProperty("cfint31.hikari.poolName", props.getProperty("hikari.poolName", "CFInt31HikariCP")));
            config.setAutoCommit(Boolean.getBoolean(props.getProperty("cfint31.hikari.auto-commit", props.getProperty("hikari.auto-commit", "true"))));

            DataSource ds = new HikariDataSource(config);

            refCFInt31DataSource.compareAndSet(null, ds);
        }
        return refCFInt31DataSource.get();
    }

    @Bean(name = "cfint31JpaProperties")
    @Primary
    public Properties cfint31JpaProperties() {
        if (cfint31JpaProperties.get() == null) {
            // Build the effective properties for cfint31
            // The persistence unit name must match the one in your persistence.xml, or you can use a dynamic unit
            Properties merged = appMergedProperties;
            String jakartaPersistenceJdbcDriver = merged.getProperty("cfint31.jakarta.persistence.jdbc.driver", merged.getProperty("jakarta.persistence.jdbc.driver", null));
            String jakartaPersistenceJdbcUrl = merged.getProperty("cfint31.jakarta.persistence.jdbc.url", merged.getProperty("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/dbtestdb"));
            String jakartaPersistenceJdbcUser = merged.getProperty("cfint31.jakarta.persistence.jdbc.user", merged.getProperty("jakarta.persistence.jdbc.user", "postgres"));
            String jakartaPersistenceJdbcPassword = merged.getProperty("cfint31.jakarta.persistence.jdbc.password", merged.getProperty("jakarta.persistence.jdbc.password", "pgpassword"));
            String jakartaPersistenceSchemaGenerationDatabaseAction = merged.getProperty("cfint31.jakarta.persistence.schema-generation.database.action", merged.getProperty("jakarta.persistence.schema-generation.database.action", null));
            String jakartaPersistenceSchemaGenerationScriptsAction = merged.getProperty("cfint31.jakarta.persistence.schema-generation.scripts.action", merged.getProperty("jakarta.persistence.schema-generation.scripts.action", null));
            String jakartaPersistenceSchemaGenerationCreateSource = merged.getProperty("cfint31.jakarta.persistence.schema-generation.create-source", merged.getProperty("jakarta.persistence.schema-generation.create-source", "metadata"));
            String jakartaPersistenceSchemaGenerationDropSource = merged.getProperty("cfint31.jakarta.persistence.schema-generation.drop-source", merged.getProperty("jakarta.persistence.schema-generation.drop-source", "metadata"));
            String jakartaPersistenceCreateDatabaseSchemas = merged.getProperty("cfint31.jakarta.persistence.create-database-schemas", merged.getProperty("jakarta.persistence.create-database-schemas", "true"));
            // String jakartaNonJtaDataSource = merged.getProperty("cfint31.jakarta.persistence.nonJtaDataSource", merged.getProperty("jakarta.persistence.nonJtaDataSource", null));
            // String jakartaJtaDataSource = merged.getProperty("cfint31.jakarta.persistence.jtaDataSource", merged.getProperty("jakarta.persistence.jtaDataSource", null));
            String hibernateDialect = merged.getProperty("cfint31.hibernate.dialect", merged.getProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"));
            String hibernateHbm2ddlAuto = merged.getProperty("cfint31.hibernate.hbm2ddl.auto", merged.getProperty("hibernate.hbm2ddl.auto", "update"));
            String hibernateShowSql = merged.getProperty("cfint31.hibernate.show_sql", merged.getProperty("hibernate.show_sql", "false"));
            String hibernateFormatSql = merged.getProperty("cfint31.hibernate.format_sql", merged.getProperty("hibernate.format_sql", "false"));
            String hibernateConnectionPoolSize = merged.getProperty("cfint31.hibernate.connection_pool_size", merged.getProperty("hibernate.connection_pool_size", "10"));
            String hibernateConnectionDatasource = merged.getProperty("cfint31.hibernate.connection_datasource", merged.getProperty("hibernate.connection_datasource", null));
            String hibernateCacheRegionFactoryClass = merged.getProperty("cfint31.hibernate.cache.region.factory_class", merged.getProperty("hibernate.cache.region.factory_class", null));
            String hibernateDefaultSchema = merged.getProperty("cfint31.hibernate.default_schema", "cfint31");
            // String hibernateTransactionJTAPlatform = merged.getProperty("cfint31.hibernate.transaction.jta.platform", merged.getProperty("hibernate.transaction.jta.platform", "org.hibernate.engine.transaction.jta.platform.internal.SpringJtaPlatform"));


            Properties applicable = new Properties();
            if (persistenceUnitName != null && !persistenceUnitName.isEmpty()) {
                applicable.setProperty("jakarta.persistence.unitName", persistenceUnitName);
            }
            if (jakartaPersistenceJdbcDriver != null && !jakartaPersistenceJdbcDriver.isEmpty()) {
                applicable.setProperty("jakarta.persistence.jdbc.driver", jakartaPersistenceJdbcDriver);
            }
            if (jakartaPersistenceJdbcUrl != null && !jakartaPersistenceJdbcUrl.isEmpty()) {
                applicable.setProperty("jakarta.persistence.jdbc.url", jakartaPersistenceJdbcUrl);
            }
            if (jakartaPersistenceJdbcUser != null && !jakartaPersistenceJdbcUser.isEmpty()) {
                applicable.setProperty("jakarta.persistence.jdbc.user", jakartaPersistenceJdbcUser);
            }
            if (jakartaPersistenceJdbcPassword != null && !jakartaPersistenceJdbcPassword.isEmpty()) {
                applicable.setProperty("jakarta.persistence.jdbc.password", jakartaPersistenceJdbcPassword);
            }
            if (jakartaPersistenceSchemaGenerationDatabaseAction != null && !jakartaPersistenceSchemaGenerationDatabaseAction.isEmpty()) {
                applicable.setProperty("jakarta.persistence.schema-generation.database.action", jakartaPersistenceSchemaGenerationDatabaseAction);
            }
            if (jakartaPersistenceSchemaGenerationScriptsAction != null && !jakartaPersistenceSchemaGenerationScriptsAction.isEmpty()) {
                applicable.setProperty("jakarta.persistence.schema-generation.scripts.action", jakartaPersistenceSchemaGenerationScriptsAction);
            }
            if (jakartaPersistenceSchemaGenerationCreateSource != null && !jakartaPersistenceSchemaGenerationCreateSource.isEmpty()) {
                applicable.setProperty("jakarta.persistence.schema-generation.create-source", jakartaPersistenceSchemaGenerationCreateSource);
            }
            if (jakartaPersistenceSchemaGenerationDropSource != null && !jakartaPersistenceSchemaGenerationDropSource.isEmpty()) {
                applicable.setProperty("jakarta.persistence.schema-generation.drop-source", jakartaPersistenceSchemaGenerationDropSource);
            }
            if (jakartaPersistenceCreateDatabaseSchemas != null && !jakartaPersistenceCreateDatabaseSchemas.isEmpty()) {
                applicable.setProperty("jakarta.persistence.create-database-schemas", jakartaPersistenceCreateDatabaseSchemas);
            }

            if (hibernateDialect != null && !hibernateDialect.isEmpty()) {
                applicable.setProperty("hibernate.dialect", hibernateDialect);
            }
            if (hibernateHbm2ddlAuto != null && !hibernateHbm2ddlAuto.isEmpty()) {
                applicable.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
            }
            if (hibernateShowSql != null && !hibernateShowSql.isEmpty()) {
                applicable.setProperty("hibernate.show_sql", hibernateShowSql);
            }
            if (hibernateFormatSql != null && !hibernateFormatSql.isEmpty()) {
                applicable.setProperty("hibernate.format_sql", hibernateFormatSql);
            }
            if (hibernateConnectionPoolSize != null && !hibernateConnectionPoolSize.isEmpty()) {
                applicable.setProperty("hibernate.connection_pool_size", hibernateConnectionPoolSize);
            }
            if (hibernateConnectionDatasource != null && !hibernateConnectionDatasource.isEmpty()) {
                applicable.setProperty("hibernate.connection.datasource", hibernateConnectionDatasource);
            }
            if (hibernateCacheRegionFactoryClass != null && !hibernateCacheRegionFactoryClass.isEmpty()) {
                applicable.setProperty("hibernate.cache.region.factory_class", hibernateCacheRegionFactoryClass);
            }
            if (hibernateDefaultSchema != null && !hibernateDefaultSchema.isEmpty()) {
                applicable.setProperty("hibernate.default_schema", hibernateDefaultSchema);
            }

            // // A JTA implementation is required as a standalone application, but you can use implementations for specific J2EE servers, such as WebLogic, too
            // if (hibernateTransactionJTAPlatform != null && !hibernateTransactionJTAPlatform.isEmpty()) {
            //     applicable.setProperty("hibernate.transaction.jta.platform", hibernateTransactionJTAPlatform);
            // }
            // // If you want to use a JTA DataSource, you can set it here
            // if (jakartaJtaDataSource != null && !jakartaJtaDataSource.isEmpty()) {
            //     applicable.setProperty("jakarta.persistence.jtaDataSource", jakartaJtaDataSource);
            // }
            // // If you want to use a non-JTA DataSource, you can set it here
            // if (jakartaNonJtaDataSource != null && !jakartaNonJtaDataSource.isEmpty()) {
            //     applicable.setProperty("jakarta.persistence.nonJtaDataSource", jakartaNonJtaDataSource);
            // }
 
            cfint31JpaProperties.compareAndSet(null, applicable);
        }
        return cfint31JpaProperties.get();
    }

    @Bean(name = "cfint31EntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory(
        @Qualifier("cfint31DataSource") DataSource cfint31DataSource,
        @Qualifier("cfint31JpaProperties") Properties cfint31JpaProperties) {
        // if (refCFInt31EntityManagerFactoryBean.get() == null) {
            // Create the EntityManagerFactory using the Jakarta Persistence API
            try {
                System.err.println("Creating cfint31EntityManagerFactory with properties:");
                cfint31JpaProperties.forEach((key, value) -> {
                    if (value instanceof String) {
                        String s = (String)value;
                        System.err.println("  " + key + " = " + s);
                    }
                    else {
                        String classname = value.getClass().getName();
                        System.err.println("  " + key + " = instanceof(" + classname + ")");
                    }
                });

                // Configure EntityManagerFactoryBean
                LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
                emfBean.setDataSource(cfint31DataSource);
                emfBean.setPackagesToScan("server.markhome.mcf.v3_1.cfint.cfint.jpa");
                emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
                emfBean.setJpaProperties(cfint31JpaProperties);
                emfBean.setPersistenceUnitName(persistenceUnitName);
                return emfBean;
                // refCFInt31EntityManagerFactoryBean.compareAndSet(null, emfBean);
            } catch (Exception e) {
                System.err.println("ERROR: Persistence.createEntityManagerFactory(\"" + persistenceUnitName + "\", emfProperties) threw " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace(System.err);
                throw e;
            }
        // }
        // return refCFInt31EntityManagerFactoryBean.get();
    }

    @Bean(name = "cfint31TransactionManager")
    @Primary
    public JpaTransactionManager cfint31TransactionManager(
        @Qualifier("cfint31EntityManagerFactory") LocalContainerEntityManagerFactoryBean cfint31EntityManagerFactory) {
            EntityManagerFactory f = cfint31EntityManagerFactory.getObject();
            if (f != null) {
                return new JpaTransactionManager(f);
            }
            else {
                System.err.println("ERROR: CFInt31JpaConfig.cfint31TransactionManager() cfint31EntityManagerFactoryBean.getObject() returned null");
				throw new CFLibNullArgumentException(getClass(), "cfint31TransactionManater", 0, "cfint31EntityManagerFactoryBean.getObject()");
            }
    }
}
