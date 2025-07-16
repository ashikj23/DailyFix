package com.dailyfix.service;

import com.dailyfix.model.User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Service
public class UserService {

    // In-memory store for demo; replace with DB (JPA) if needed
    private final Map<String, User> userStore = new HashMap<>();

    public User findOrCreateUser(Principal principal) {
        String email = principal.getName();  // Usually the user's email from OAuth2
        return userStore.computeIfAbsent(email, e -> {
            User user = new User();
            user.setEmail(email);
            String displayName = "New User";
            if (principal instanceof OAuth2AuthenticationToken token) {
                OAuth2User oauth2User = token.getPrincipal();
                Object nameAttr = oauth2User.getAttributes().get("name");
                if (nameAttr != null) {
                    displayName = nameAttr.toString();
                }
            }
            user.setDisplayName(displayName);
            return user;
        });
    }
}
