package raf.si.bolnica.management.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.interceptors.UserInfoInterceptor;
import raf.si.bolnica.management.jwt.JwtProperties;

import java.util.Base64;

@Configuration
public class SpringWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
        registry.addInterceptor(new UserInfoInterceptor(loggedInUser, secretKey));
    }
}
