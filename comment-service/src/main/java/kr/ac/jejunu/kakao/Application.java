package kr.ac.jejunu.kakao;

import kr.ac.jejunu.kakao.session.SessionHandlerInterceptorAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by HSH on 16. 5. 28..
 */
@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class Application extends WebMvcConfigurerAdapter {

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.enableContentNegotiation(new MappingJackson2JsonView());
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DateTimeProvider dateTimeProvider() { //@CreateDate를 사용하기 위해 사용하는?
        return new DateTimeProvider() {
            @Override
            public Calendar getNow() {
                return GregorianCalendar.getInstance();
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionHandlerInterceptorAdapter()).addPathPatterns("/write", "/like/*", "/dislike/*").excludePathPatterns("/login", "/signup", "/");
    }
}
