package kr.ac.jejunu.kakao.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by HSH on 16. 6. 2..
 */
public class SessionHandlerInterceptorAdapter implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SessionHandlerInterceptorAdapter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("name") == null) {
            response.sendRedirect("/login");
            return false;
        }
        logger.info("***************preHandle*****************");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("***************postHandle*****************");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("***************afterComplation*****************");
    }
}
