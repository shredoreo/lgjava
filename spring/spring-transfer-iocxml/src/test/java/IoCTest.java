import com.shred.spring.dao.AccountDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class IoCTest {

    @Test
    public void testIoC(){
        //通过classpath下的xml启动，推荐使用
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        //不推荐使用
//        new FileSystemXmlApplicationContext()

        AccountDao accountDao = (AccountDao) applicationContext.getBean("accountDao");
        System.out.println(accountDao);


    }
}
