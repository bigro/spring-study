import hello.Application;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class PerformanceTest {

    @Test
    public void test() throws Exception {
        long start = System.currentTimeMillis();
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        long end = System.currentTimeMillis();
        System.out.println("end:" + (end - start));
    }
}
