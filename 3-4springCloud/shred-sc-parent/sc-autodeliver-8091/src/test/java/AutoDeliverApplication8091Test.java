import com.shred.sc.AutoDeliverApplication8091;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest(classes = {AutoDeliverApplication8091.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AutoDeliverApplication8091Test {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Test
    public void test(){
        List<ServiceInstance> instances = discoveryClient.getInstances("sc-resume");
        for (ServiceInstance instance : instances) {
            System.out.println(instance);
        }

    }
}
