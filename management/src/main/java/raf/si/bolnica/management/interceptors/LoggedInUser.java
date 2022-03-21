package raf.si.bolnica.management.interceptors;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoggedInUser {

    private String username;

    private Set<String> roles;

    public LoggedInUser() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
