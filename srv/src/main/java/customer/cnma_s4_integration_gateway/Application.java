package customer.cnma_s4_integration_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import customer.cnma_s4_integration_gateway.adapters.config.AdapterConfig;


@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@EnableConfigurationProperties(AdapterConfig.class)
@SpringBootApplication(exclude = {
  DataSourceAutoConfiguration.class,
  HibernateJpaAutoConfiguration.class,
  DataSourceTransactionManagerAutoConfiguration.class
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
