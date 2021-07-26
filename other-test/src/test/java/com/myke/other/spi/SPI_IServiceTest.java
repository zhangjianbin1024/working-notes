package com.myke.other.spi;

import org.apache.catalina.LifecycleException;
import org.junit.Test;
import org.springframework.core.io.support.SpringFactoriesLoader;

import javax.servlet.ServletContainerInitializer;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SPI 测试
 * 1. jdk SPI
 * 2. tomcat SPI
 * 3. spring SPI
 */
public class SPI_IServiceTest {

    /**
     * ServiceLoader可以根据IService把定义的两个实现类找出来
     * <p>
     * 加载配置文件：META-INF/services/
     */
    @Test
    public void testSPI_IService() {
        // 加载IService下所有的服务
        ServiceLoader<SPI_IService> serviceLoader = ServiceLoader.load(SPI_IService.class);

        for (SPI_IService service : serviceLoader) {
            System.out.println("toString" + "=" + service.toString() + " " + service.getScheme() + "=" + service.sayHello());
        }
    }

    /**
     * tomcat 的SPI实现类 WebappServiceLoader(模仿jdk中的ServiceLoader类 )
     * 它用于比如容器启动时加载所有的ServletContainerInitializer实现类，
     * 从而驱动Spring容器的启动。
     * <p>
     * 加载配置文件：META-INF/services/
     */
    @Test
    public void testSPI_ServletContainerInitializer() throws LifecycleException {
        // ServletContainerInitializer 是 Servlet 3.0 新增的一个接口，
        // 主要用于在容器启动阶段通过编程风格注册Filter, Servlet以及Listener，
        // 以取代通过web.xml配置注册。这样就利于开发内聚的web应用框架.

        // ServiceLoader 也可以获取到 ServletContainerInitializer 的实现类
        ServiceLoader<ServletContainerInitializer> serviceLoader = ServiceLoader.load(ServletContainerInitializer.class);
        for (ServletContainerInitializer service : serviceLoader) {
            System.out.println(service.toString());
        }
    }


    /**
     * SpringFactoriesLoader它用于SpringBoot中的自动化配置起到了关键甚至决定性作用
     * <p>
     * spring.factories内容的key可以是接口，也可以是抽象类、具体的类。但是有个原则：=后面必须是key的实现类（子类）
     * <p>
     * spring boot 配置文件路径: spring-boot-2.1.6.RELEASE.jar!\META-INF\spring.factories
     */
    @Test
    public void testSPI_SpringFactoriesLoader() {
        //loadFactories拿到全类名后会立马实例化
        List<SPI_IService> services = SpringFactoriesLoader.loadFactories(SPI_IService.class, this.getClass().getClassLoader());
        System.out.println(services);//[com.myke.other.spi.impl.HDFSService@5700d6b1, com.myke.other.spi.impl.LocalService@6fd02e5]


        //loadFactoryNames方法只拿全类名
        List<String> list = SpringFactoriesLoader.loadFactoryNames(SPI_IService.class, this.getClass().getClassLoader());
        System.out.println(list);//[com.myke.other.spi.impl.HDFSService, com.myke.other.spi.impl.LocalService]


    }

}