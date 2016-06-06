/**
 * Created by HSH on 16. 6. 6..
 */

import kr.ac.jejunu.kakao.Application;
import kr.ac.jejunu.kakao.model.Comment;
import kr.ac.jejunu.kakao.repository.CommentRepository;
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


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
@WebAppConfiguration
public class CommentTest {
    @Autowired
    private CommentRepository commentRepository;
    private String content;

    @Before
    public void setup(){
        Comment comment = new Comment();
        comment.setId(1);
        content = "첫 댓글입니다.";
        comment.setContent(content);

        commentRepository.save(comment);
    }

    @Test
    public void getComment(){
        Comment getComment = commentRepository.findOne(1);
        assertThat(getComment.getContent(), is(content));
    }

    @Test
    public void addComment(){
        String content = "두번째 댓글입니다.";

        Comment newCommnet = new Comment();
        newCommnet.setId(2);
        newCommnet.setContent(content);
        commentRepository.save(newCommnet);

        Comment getCommnet = commentRepository.findOne(2);
        assertThat(content, is(getCommnet.getContent()));
    }

    @Test
    public void updateComment(){
        String content = "업데이트된 댓글입니다.";
        Comment comment = commentRepository.findOne(1);
        comment.setContent(content);
        commentRepository.save(comment);

        Comment updatedCommnet = commentRepository.findOne(1);
        assertThat(content, is(updatedCommnet.getContent()));
    }

    @Test
    public void deleteComment(){
        commentRepository.delete(1);
        Comment commnet = commentRepository.findOne(1);
        assertThat(commnet, is(nullValue()));
    }

}
