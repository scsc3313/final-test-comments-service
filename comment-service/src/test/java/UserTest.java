import kr.ac.jejunu.kakao.Application;
import kr.ac.jejunu.kakao.model.User;
import kr.ac.jejunu.kakao.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by HSH on 16. 6. 6..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
@WebAppConfiguration
public class UserTest {

    @Autowired
    private UserRepository userRepository;
    private String setupUserId;


    @Before
    public void setup() {
        User setupUser = new User();
        setupUserId = "first";
        setupUser.setUserId(setupUserId);
        userRepository.save(setupUser);
    }

    @Test
    public void getUser(){
        assertThat(setupUserId, is(userRepository.findOne(setupUserId).getUserId()));
    }

    @Test
    public void addUser() {
        User newUser = new User();
        newUser.setUserId("admin");
        userRepository.save(newUser);
        User resultUser = userRepository.findOne("admin");
        assertThat(newUser.getUserId(), is(resultUser.getUserId()));
    }

    @Test
    public void updateUser() {
        User getUser= userRepository.findOne(setupUserId);
        String name = "현승호";
        getUser.setName(name);
        userRepository.save(getUser);

        User updatedUser = userRepository.findOne(setupUserId);
        assertThat(name, is(updatedUser.getName()));
    }

    @Test
    public void deleteUser(){
        userRepository.delete(setupUserId);
        User deletedUser = userRepository.findOne(setupUserId);
        assertThat(deletedUser, is(nullValue()));
    }
}
