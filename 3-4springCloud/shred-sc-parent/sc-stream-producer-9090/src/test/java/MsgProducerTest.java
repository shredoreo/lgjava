import com.shred.sc.StreamProducerApplication9090;
import com.shred.sc.service.IMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes= {StreamProducerApplication9090.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MsgProducerTest {

    @Autowired
    private IMessageProducer iMessageProducer;

    @Test
    public void test(){
        iMessageProducer.sendMessage("hello world");
    }
}
