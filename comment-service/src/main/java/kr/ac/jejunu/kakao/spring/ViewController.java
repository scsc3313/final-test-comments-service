package kr.ac.jejunu.kakao.spring;

import kr.ac.jejunu.kakao.model.Comment;
import kr.ac.jejunu.kakao.model.User;
import kr.ac.jejunu.kakao.repository.CommentRepository;
import kr.ac.jejunu.kakao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private int pageNum;

    private final static Logger logger = LoggerFactory.getLogger(ViewController.class);

    @RequestMapping("/")
    public String index(Model model, @ModelAttribute(value = "comments") Page<Comment> comments) {
        model.addAttribute("comments", comments);
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
        } else {
            url = "login";
        }
        return url;
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "input-form";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(User user, HttpSession httpSession, Model model) {
        if (httpSession.getAttribute("id") != null) { //update 상황
            User getUser = userRepository.findOne(user.getUserId()); // 비밀번호가 일치하는지 확인
            if (!getUser.getPassword().equals(user.getPassword())){ //비밀번호가 같지 않으면 input-form 리턴
                model.addAttribute("error", "패스워드가 일치하지 않습니다.");
                return "input-form";
            }
        }
        userRepository.save(user); //일반 signup상황
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
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @ModelAttribute("comments")
    public Page<Comment> comments(@PageableDefault(size = 7, direction = Sort.Direction.DESC, sort = "id") Pageable pageable) {
        if (pageNum < 1 || pageNum > 1000) pageNum = 0;
        Page<Comment> commentList = commentRepository.findAll(pageable);
        if (pageNum > commentList.getTotalPages()) pageNum = 0;
        return commentList;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") Integer id) {
        commentRepository.delete(id);
        return "redirect:/";
    }

    @RequestMapping("/update/{userId}")
    public String update(@PathVariable(value = "userId") String id, Model model) {
        model.addAttribute("user", userRepository.findOne(id));
        return "input-form";
    }

    @RequestMapping("/like/{id}")
    public String addLike(@PathVariable(value = "id") Integer id) {
        Comment comment = commentRepository.findOne(id);
        Integer count = comment.getLikeCount();
        count++;
        comment.setLikeCount(count);
        commentRepository.save(comment);
        return "index";
    }

    @RequestMapping("/dislike/{id}")
    public String addDislike(@PathVariable(value = "id") Integer id) {
        Comment comment = commentRepository.findOne(id);
        Integer count = comment.getDislikeCount();
        count++;
        comment.setDislikeCount(count);
        commentRepository.save(comment);
        return "redirect:/";
    }


    private void setSession(User user, HttpSession httpSession) {
        httpSession.setAttribute("id", user.getUserId());
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("des", user.getDescription());
    }


}
