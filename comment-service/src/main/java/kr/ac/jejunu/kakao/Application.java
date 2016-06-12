package kr.ac.jejunu.kakao;

import kr.ac.jejunu.kakao.session.SessionHandlerInterceptorAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by HSH on 16. 5. 28..
 */
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.enableContentNegotiation(new MappingJackson2JsonView());
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionHandlerInterceptorAdapter()).addPathPatterns("/write", "/like/*", "/dislike/*").excludePathPatterns("/login", "/signup", "/");
    }
}
