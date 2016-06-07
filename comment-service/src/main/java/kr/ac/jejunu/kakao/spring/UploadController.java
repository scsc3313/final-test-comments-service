package kr.ac.jejunu.kakao.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HSH on 16. 6. 7..
 */
@Controller
public class UploadController {

    @RequestMapping("/upload")
    public String upload(){
        return "upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("src/main/webapp/WEB-INF/views/spring/resources/images/" + file.getOriginalFilename()));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(file.getBytes());
        bufferedOutputStream.close();
        model.addAttribute("url", "/resources/images/" + file.getOriginalFilename());
        return "upload";
    }
}
