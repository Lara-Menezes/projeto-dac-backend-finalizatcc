package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.controllers.AuthController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        HttpSession session = request.getSession(false);
        boolean authenticated = session != null
                && session.getAttribute(AuthController.AUTHENTICATED_USER) != null;

        if (!authenticated) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Sessao ausente ou expirada");
            return false;
        }

        return true;
    }
}
