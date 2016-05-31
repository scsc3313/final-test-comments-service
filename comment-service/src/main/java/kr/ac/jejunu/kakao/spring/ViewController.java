package kr.ac.jejunu.kakao.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by HSH on 16. 5. 31..
 */
@Controller
public class ViewController {

    private final static Logger logger = LoggerFactory.getLogger(ViewController.class);

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("name", "Seungho!");
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/signup")
    public String signup(){
        return "signup";
    }

}
