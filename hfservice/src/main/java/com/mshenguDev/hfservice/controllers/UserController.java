package com.mshenguDev.hfservice.controllers;

import com.mshenguDev.hfservice.entities.Dto.LoginDto;
import com.mshenguDev.hfservice.entities.Dto.AuthenticatedUserDto;
import com.mshenguDev.hfservice.entities.Dto.UserDto;
import com.mshenguDev.hfservice.entities.User;
import com.mshenguDev.hfservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RestController
//@RequestMapping(path = "/api/v0.1/users")
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;
    private final SecurityContextRepository securityContextRepository;

    public UserController(
            UserService userService,
            SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping(path = "/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto){
        String result = userService.registerUser(userDto);
        if(!result.equals("SUCCESS")){
            return ResponseEntity.badRequest().body("Error while adding user");
        }
        return ResponseEntity.ok("New user Added");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticatedUserDto> loginUser(
            @RequestBody LoginDto logDetails,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = userService.login(logDetails);
        String authority =
                "ROLE_" + (user.getRole() == null ? "CUSTOMER" : user.getRole().name());
        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                user.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority(authority)));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        request.getSession(true);
        request.changeSessionId();
        securityContextRepository.saveContext(context, request, response);

        return ResponseEntity.ok(AuthenticatedUserDto.from(user));
    }

    @GetMapping(path = "/csrf")
    public ResponseEntity<Map<String, String>> csrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(Map.of("token", csrfToken.getToken()));
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<List<User>> getUsers(){
        return  ResponseEntity.ok(userService.retrieveAllUsers());
    }

    @GetMapping(path = "/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.retrieveUserById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @DeleteMapping(path = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
       String response =  userService.removeUser(id);
        if(response.isEmpty()){
            throw new UnsupportedOperationException("User not found");
        }
        return ResponseEntity.ok("User deleted");
    }

}
