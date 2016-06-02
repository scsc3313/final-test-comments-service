package kr.ac.jejunu.kakao.model;


import javax.persistence.*;

/**
 * Created by HSH on 16. 5. 28..
 */
@Entity
@Table(name = "userinfo")
public class User {

    @Id
    private String userId;
    private String name;
    private String password;
    private String description;
    private String userProfileImage;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
