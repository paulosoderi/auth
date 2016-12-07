package com.psoderi.autenticacao.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.psoderi.autenticacao")
@EnableSwagger2
@Configuration
@EnableTransactionManagement
public class Application{
	


	public static void main(String[] args) {
		 SpringApplication.run(Application.class, args);
	}
	
	//INICIO - Configuracao do datasource	

	private EmbeddedDatabase ed;

    @Bean(name="hsqlInMemory")
	public EmbeddedDatabase hsqlInMemory() {

        if ( this.ed == null ) {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            this.ed = builder.setType(EmbeddedDatabaseType.HSQL).build();
        }

        return this.ed;        
	}
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){

	    LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();

        lcemfb.setDataSource(this.hsqlInMemory());
        lcemfb.setPackagesToScan(new String[] {"com.avenuecode.moviescriptscanner"});

        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
		lcemfb.setJpaVendorAdapter(va);

		Properties ps = new Properties();
		ps.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		ps.put("hibernate.hbm2ddl.auto", "update");
		ps.put("hibernate.show_sql", "true");
		ps.put("hibernate.connection.username", "sa");
		ps.put("hibernate.connection.password", "");
		lcemfb.setJpaProperties(ps);

		lcemfb.afterPropertiesSet();

        return lcemfb;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){

        JpaTransactionManager tm = new JpaTransactionManager();

	    tm.setEntityManagerFactory(this.entityManagerFactory().getObject());

        return tm;		
    }
   
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
	  //FIM - Configuracao do datasource
 
	    
	  //INICIO - Swagger
	    @Bean
	    public Docket swaggerSettings() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                .apis(RequestHandlerSelectors.any())
	                .paths(PathSelectors.regex("\\/api\\/[a-z/A-Z0-9]*"))
	                //.paths(PathSelectors.any())
	                .build()
	                .pathMapping("/");
	    }
	  //FIM - Swagger
	    
	    
	    public static Properties getProp() throws IOException {
	    	final Resource resource = new ClassPathResource("database.properties");
	        final Properties props = PropertiesLoaderUtils.loadProperties(resource);
/*
			Properties props = new Properties();
			FileInputStream file = new FileInputStream(
					"database.properties");
			props.load(file);*/
			return props;

		}
}
