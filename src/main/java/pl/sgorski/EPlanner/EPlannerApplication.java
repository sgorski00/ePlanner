package pl.sgorski.EPlanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EPlannerApplication.class, args);
	}

}
