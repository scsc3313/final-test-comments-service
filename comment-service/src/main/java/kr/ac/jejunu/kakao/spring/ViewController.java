package kr.ac.jejunu.kakao.spring;

import kr.ac.jejunu.kakao.model.User;
import kr.ac.jejunu.kakao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by HSH on 16. 5. 31..
 */
@Controller
@SessionAttributes("userModel")
public class ViewController {

    @Autowired
    public UserRepository userRepository;

    private final static Logger logger = LoggerFactory.getLogger(ViewController.class);

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(User inputUser, HttpSession session) {
        User realUser = userRepository.findOne(inputUser.getUserId());
        String url;
        if (realUser != null && realUser.getPassword() == inputUser.getPassword()) {
            setSession(realUser, session);
            url = "index";
        } else
            url = "login";
        return url;
    }

    @RequestMapping("/signup")
    public String signup() {
        return "input-form";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(User user, HttpSession httpSession) {
        userRepository.save(user);
        setSession(user, httpSession);
        return "index";
    }

    @RequestMapping("/write")
    public String write() {
        return "write";
    }

    @ModelAttribute("userModel")
    public User model() {
        return new User();
    }

    private void setSession(User user, HttpSession httpSession) {
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("des", user.getDescription());
    }


}
