package kr.ac.jejunu.kakao.spring;

import kr.ac.jejunu.kakao.model.Comment;
import kr.ac.jejunu.kakao.model.Eval;
import kr.ac.jejunu.kakao.model.User;
import kr.ac.jejunu.kakao.repository.CommentRepository;
import kr.ac.jejunu.kakao.repository.EvalRepository;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by HSH on 16. 5. 31..
 */
@Controller
@SessionAttributes("userModel")
public class AppController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EvalRepository evalRepository;

    private int pageNum;

    private final static Logger logger = LoggerFactory.getLogger(AppController.class);

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    private void formatDate(@ModelAttribute(value = "comments") Page<Comment> comments) {
        String resultDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        for (Comment comment : comments) {
            Long compare = (System.currentTimeMillis() - Long.valueOf(comment.getDate())) / 1000;
            if (compare <= 60)
                resultDate = "방금전";
            else if (compare <= 3600)
                resultDate = compare / 60 + "분전";
            else if (compare <= 86400)
                resultDate = compare / 3600 + "시간전";
            else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(comment.getDate()));
                resultDate = format.format(calendar.getTime());
            }
            comment.setDate(resultDate);
        }
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
    public String signup(@RequestParam Map mapUser, HttpSession httpSession, @RequestParam(value = "userProfileImage") MultipartFile file, Model model) throws IOException {

        User user = mappingUser(mapUser);

        uploadImage(file, model, user);

        if (user.getUserId() == "" || user.getPassword() == "" || user.getName() == "") {
            model.addAttribute("signup", "아이디, 패스워드, 이름은 필수항목입니다.");
            return "errors";
        }
        Iterable<User> users = userRepository.findAll();
        for (User getUser : users) {
            if (getUser.getUserId().equals(user.getUserId())) {
                model.addAttribute("signup", "아이디가 중복됩니다.");
                return "errors";
            }
            if (getUser.getName().equals(user.getName())) {
                model.addAttribute("signup", "이름이 중복됩니다.");
                return "error";
            }
        }
        userRepository.save(user);
        setSession(user, httpSession);
        return "redirect:/";
    }

    @RequestMapping("/update/{userId}")
    public String update(@PathVariable(value = "userId") String id, Model model) {
        model.addAttribute("user", userRepository.findOne(id));
        return "input-form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam Map mapUser, HttpSession httpSession, @RequestParam(value = "userProfileImage") MultipartFile file, Model model) throws IOException {

        User user = mappingUser(mapUser);

        if (httpSession.getAttribute("id") != null) { //update 상황
            User getUser = userRepository.findOne(user.getUserId()); // 비밀번호가 일치하는지 확인
            if (!getUser.getPassword().equals(user.getPassword())) { //비밀번호가 같지 않으면 input-form 리턴
                model.addAttribute("update", "패스워드가 일치하지 않습니다.");
                return "errors";
            }
        }
        if(!uploadImage(file, model, user)){
            user.setUserProfileImage(userRepository.findOne(user.getUserId()).getUserProfileImage());
        }
        setSession(user, httpSession);
        userRepository.save(user);
        return "redirect:/";
    }

    private User mappingUser(Map mapUser) {
        User user = new User();
        user.setUserId((String) mapUser.get("userId"));
        user.setName((String) mapUser.get("name"));
        user.setPassword((String) mapUser.get("password"));
        user.setDescription((String) mapUser.get("description"));
        return user;
    }

    private boolean uploadImage(MultipartFile file, Model model, User user) throws IOException {
        if (!file.getOriginalFilename().equals("")) {
            user.setUserProfileImage(file.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(new File("src/main/webapp/WEB-INF/views/spring/resources/images/" + file.getOriginalFilename()));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(file.getBytes());
            bufferedOutputStream.close();
            model.addAttribute("url", "/resources/" + file.getOriginalFilename());
            return true;
        }
        return false;
    }

    @RequestMapping("/write")
    public String write() {
        return "write";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String write(@RequestParam(value = "content") String content, HttpSession session, Model model) {
        User user = userRepository.findOne(String.valueOf(session.getAttribute("id")));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(content);
        comment.setDate(String.valueOf(new Date().getTime()));
        commentRepository.save(comment);
        model.addAttribute("writeDone", "");
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @ModelAttribute("userModel")
    public User model() {
        return new User();
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
        Iterable<Eval> evals = evalRepository.findAll();
        for (Eval eval : evals) {
            if (eval.getComment().getId().equals(id))
                evalRepository.delete(eval.getEvalId());
        }
        commentRepository.delete(id);
        return "redirect:/";
    }

    @RequestMapping("/like/{id}")
    public String addLike(@PathVariable(value = "id") Integer id, HttpSession session, Model model) {
        if (checkDuplicate(id, session, model)) return "index";

        Comment comment = commentRepository.findOne(id);
        Integer count = comment.getLikeCount();
        count++;
        comment.setLikeCount(count);
        commentRepository.save(comment);

        addEval(session, comment, true);
        return "redirect:/";
    }

    @RequestMapping("/dislike/{id}")
    public String addDislike(@PathVariable(value = "id") Integer id, HttpSession session, Model model) {
        if (checkDuplicate(id, session, model)) return "index";

        Comment comment = commentRepository.findOne(id);
        Integer count = comment.getDislikeCount();
        count++;
        comment.setDislikeCount(count);
        commentRepository.save(comment);

        addEval(session, comment, false);
        return "redirect:/";
    }

    private boolean checkDuplicate(@PathVariable(value = "id") Integer id, HttpSession session, Model model) {
        Iterable<Eval> evals = evalRepository.findAll();
        for (Eval eval : evals) {
            if (eval.getUser().getUserId().equals(session.getAttribute("id"))) {
                if (eval.getComment().getId().equals(id)) {
                    model.addAttribute("duplicate", "이미 평가하셨습니다.");
                    return true;
                }
            }
        }
        return false;
    }

    private void addEval(HttpSession session, Comment comment, boolean reco) {
        Eval eval = new Eval();
        eval.setUser(userRepository.findOne((String) session.getAttribute("id")));
        eval.setComment(comment);
        eval.setRecommend(reco);
        evalRepository.save(eval);
    }

    private void setSession(User user, HttpSession httpSession) {
        httpSession.setAttribute("id", user.getUserId());
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("des", user.getDescription());
    }
}
