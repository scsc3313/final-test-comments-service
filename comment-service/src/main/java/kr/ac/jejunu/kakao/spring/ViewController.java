package kr.ac.jejunu.kakao.spring;

import kr.ac.jejunu.kakao.model.Comment;
import kr.ac.jejunu.kakao.model.User;
import kr.ac.jejunu.kakao.repository.CommentRepository;
import kr.ac.jejunu.kakao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by HSH on 16. 5. 31..
 */
@Controller
@SessionAttributes("userModel")
public class ViewController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CommentRepository commentRepository;

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
        if (realUser != null && realUser.getPassword().equals(inputUser.getPassword())) {
            setSession(realUser, session);
            url = "redirect:/";
        } else{
            url = "login";
        }
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
        return "redirect:/";
    }

    @RequestMapping("/write")
    public String write() {
        return "write";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String write(@RequestParam(value = "content") String content, HttpSession session) {
        User user = userRepository.findOne(String.valueOf(session.getAttribute("id")));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(content);
        comment.setDate(new Date());
        commentRepository.save(comment);
        return "redirect:/";
    }

    @ModelAttribute("userModel")
    public User model() {
        return new User();
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/";
    }

    private void setSession(User user, HttpSession httpSession) {
        httpSession.setAttribute("id", user.getUserId());
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("des", user.getDescription());
    }


}
