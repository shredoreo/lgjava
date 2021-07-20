import com.shred.ssm.pojo.Account;
import com.shred.ssm.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

// 指定spring的运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 指定配置文件，就不再需要自己初始化容器了
@ContextConfiguration(locations = {"classpath*:application*.xml"})
public class MybatisSpringTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testMybatisSpring(){
        List<Account> accounts = accountService.queryAccountList();
        accounts.forEach(System.out::println);
    }

}
