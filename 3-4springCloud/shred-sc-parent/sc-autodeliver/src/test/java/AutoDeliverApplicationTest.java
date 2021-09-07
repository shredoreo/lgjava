import com.netflix.discovery.converters.Auto;
import com.shred.sc.AutoDeliverApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest(classes = {AutoDeliverApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AutoDeliverApplicationTest {

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
