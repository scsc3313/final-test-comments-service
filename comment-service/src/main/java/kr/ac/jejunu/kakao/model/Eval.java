package kr.ac.jejunu.kakao.model;

import javax.persistence.*;

/**
 * Created by HSH on 16. 6. 6..
 */
@Entity
public class Eval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer evalId;
    @JoinColumn(name = "id")
    @ManyToOne
    private Comment comment;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
    private boolean recommend;

    public Integer getEvalId() {
        return evalId;
    }

    public void setEvalId(Integer evalId) {
        this.evalId = evalId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}
