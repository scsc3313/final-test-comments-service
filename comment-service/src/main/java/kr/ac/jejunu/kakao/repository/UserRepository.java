package kr.ac.jejunu.kakao.repository;

import kr.ac.jejunu.kakao.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by HSH on 16. 6. 2..
 */
public interface UserRepository extends CrudRepository<User, String> {
}
