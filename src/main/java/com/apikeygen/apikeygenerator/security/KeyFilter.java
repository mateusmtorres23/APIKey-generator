package com.apikeygen.apikeygenerator.security;

import com.apikeygen.apikeygenerator.model.Key;
import com.apikeygen.apikeygenerator.model.User; // Import User
import com.apikeygen.apikeygenerator.repository.UserRepository; // Import UserRepository
import com.apikeygen.apikeygenerator.service.KeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
// UserDetailsService is no longer needed
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional; // <-- Make sure this is imported

@Component
public class KeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final KeyService keyService;
    private final UserRepository userRepository; // Use UserRepository

    // Constructor uses UserRepository
    public KeyFilter(KeyService keyService, UserRepository userRepository) {
        this.keyService = keyService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String rawApiKey = request.getHeader(API_KEY_HEADER);

        if (rawApiKey == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Optional<Key>  keyOptional = keyService.validateKey(rawApiKey);

        if (keyOptional.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            Key validKey = keyOptional.get();

            Optional<User> userOptional = userRepository.findById(validKey.ownerId());

            if (userOptional.isPresent()) {
                UserDetails userDetails = userOptional.get();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
