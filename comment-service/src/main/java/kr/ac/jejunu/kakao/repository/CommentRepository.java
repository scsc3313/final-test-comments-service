package kr.ac.jejunu.kakao.repository;

import kr.ac.jejunu.kakao.model.Comment;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by HSH on 16. 5. 28..
 */
public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
}
