package site.hansi.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 北京智匠坊
 */
@SpringBootApplication(scanBasePackages = {"${bjzjf.info.base-package}.server", "${bjzjf.info.base-package}.module"})
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }

}
