package com.example.akela.swim.crm.security;

import com.example.akela.swim.crm.user.UserStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class PasswordChangeRequiredFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "/api/auth/login",
            "/api/auth/me",
            "/api/auth/change-password"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // lasam OPTIONS si allowed paths
        if (HttpMethod.OPTIONS.matches(request.getMethod()) || ALLOWED_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AklUserPrincipal p) {
            if (p.getStatus() == UserStatus.PENDING_PASSWORD) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("""
                    {"code":"PASSWORD_CHANGE_REQUIRED","message":"Trebuie sa iti schimbi parola inainte sa folosesti aplicatia."}
                """);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
