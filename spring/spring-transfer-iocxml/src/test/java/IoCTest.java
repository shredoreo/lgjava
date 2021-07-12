import com.shred.spring.SpringConfig;
import com.shred.spring.dao.AccountDao;
import com.shred.spring.service.TransferService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IoCTest {

    @Test
    public void testIoC(){
        //通过classpath下的xml启动，推荐使用
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        //不推荐使用
//        new FileSystemXmlApplicationContext()

        AccountDao accountDao = (AccountDao) applicationContext.getBean("accountDao");
        System.out.println(accountDao);

//        Object createBeanFactory = applicationContext.getBean("createBeanFactory");
//        System.out.println(createBeanFactory);

        applicationContext.getBean("dataSource");

        applicationContext.close();

    }

    @Test
    public void testAnno(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        Object lazyResult = context.getBean("lazyResult");
        System.out.println(lazyResult);

        System.out.println(context.getBean("companyBean"));
        /*通过&获取factoryBean*/
        System.out.println(context.getBean("&companyBean"));
        //Company(name=nnnn, scale=ff, address=ssfd)
        //com.shred.spring.factory.CompanyFactoryBean@74a6f9c1
        context.close();
    }

    @Test
    public void testXMLAop() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        TransferService bean = applicationContext.getBean(TransferService.class);
        bean.transfer("6029621011001","6029621011000", 12);
    }
}
