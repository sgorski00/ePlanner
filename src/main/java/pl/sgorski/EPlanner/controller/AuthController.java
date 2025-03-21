package pl.sgorski.EPlanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sgorski.EPlanner.dto.LoginRequest;
import pl.sgorski.EPlanner.dto.LoginResponse;
import pl.sgorski.EPlanner.dto.RegisterRequest;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.service.UserService;
import pl.sgorski.EPlanner.service.utils.JwtUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        ApplicationUser user = userService.findByUsername(loginRequest.getUsername());
        String token = jwtUtils.generateToken(user);
        LoginResponse loginResponse = new LoginResponse(
                "Log in finished successfully",
                token
        );
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApplicationUser> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        ApplicationUser user = userService.register(registerRequest);

        return ResponseEntity.ok(user);
    }
}
