import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("src/main/resources/beans.xml");
        RoboFactory roboFactory = (RoboFactory)context.getBean("factory");

        roboFactory.runProduction();
        System.out.println(context.getBeanDefinitionCount());
        System.out.println(Arrays.stream(context.getBeanDefinitionNames()).toArray());
    }
}
