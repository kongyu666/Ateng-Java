package local.kongyu.MySpringBootAdmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class MySpringBootAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootAdminApplication.class, args);
    }

}
