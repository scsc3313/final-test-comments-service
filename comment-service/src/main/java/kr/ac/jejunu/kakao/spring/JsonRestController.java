package kr.ac.jejunu.kakao.spring;

import kr.ac.jejunu.kakao.model.Comment;
import kr.ac.jejunu.kakao.repository.CommentRepository;
import kr.ac.jejunu.kakao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by HSH on 16. 5. 28..
 */
@RestController
public class JsonRestController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public ResponseEntity<?> list(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "15") Integer size) {
        Page<Comment> commments = commentRepository.findAll(new PageRequest(page, size));
        return new ResponseEntity<>(commments, HttpStatus.OK);
    }

    @RequestMapping(value = "/comment/", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> save(@RequestBody Comment comment) {
        Comment result = commentRepository.save(comment);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/comment/{id}", method = {RequestMethod.GET})
    public ResponseEntity<?> save(@PathVariable("id") Integer id) {
        Comment result = commentRepository.findOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
