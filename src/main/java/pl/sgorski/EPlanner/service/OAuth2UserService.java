package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Role;
import pl.sgorski.EPlanner.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public OAuth2UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        ApplicationUser user = getUserAndSaveIfNotExist(email, provider, providerId);

        return new DefaultOAuth2User(
                user.getAuthorities(),
                oAuth2User.getAttributes(),
                "email"
        );
    }

    private ApplicationUser getUserAndSaveIfNotExist(String email, String provider, String providerId) {
        Optional<ApplicationUser> oUser = userRepository.findByUsername(email);
        if(oUser.isEmpty()) {
            ApplicationUser user = new ApplicationUser();
            user.setEmail(email);
            user.setUsername(email);
            user.setProvider(provider);
            user.setProviderId(providerId);

            Role userRole = roleService.getRoleByName("user");
            user.setRole(userRole);
            return userRepository.save(user);
        }
        return oUser.get();
    }
}
