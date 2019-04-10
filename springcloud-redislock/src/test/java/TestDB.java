import com.lideshuai.RedisApplication;
import com.lideshuai.entiey.User;
import com.lideshuai.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestDB {

    @Autowired
    private UserService userService;

    @Test
    public void test (){
        User user = new User();
        user.setName("lideshuai");
        user.setAge(20);

        try {
            userService.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
