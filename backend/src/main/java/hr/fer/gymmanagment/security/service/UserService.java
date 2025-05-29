package hr.fer.gymmanagment.security.service;

import hr.fer.gymmanagment.GymManagmentException;
import hr.fer.gymmanagment.security.entity.User;
import hr.fer.gymmanagment.security.entity.dto.request.LoginRequest;
import hr.fer.gymmanagment.security.entity.dto.request.SignupRequest;
import hr.fer.gymmanagment.security.entity.dto.response.JwtResponse;
import hr.fer.gymmanagment.security.entity.dto.response.UserResponse;
import hr.fer.gymmanagment.security.entity.pojo.DashboardUserDetails;
import hr.fer.gymmanagment.security.entity.pojo.JwtGenerationResult;
import hr.fer.gymmanagment.security.entity.pojo.RoleEnum;
import hr.fer.gymmanagment.security.repository.RoleRepository;
import hr.fer.gymmanagment.security.repository.UserRepository;
import hr.fer.gymmanagment.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    protected void addDefaultUsers() {
        Optional<User> pero = userRepository.findByEmail("pero@gmail.com");

        if (pero.isPresent()) {
            User user = pero.get();
            if (user.getPassword().equals("test")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
        Optional<User> jura = userRepository.findByEmail("jura@gmail.com");
        if (jura.isPresent()) {
            User user = jura.get();
            if (user.getPassword().equals("test")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
        Optional<User> zdravko= userRepository.findByEmail("zdravko@gmail.com");
        if (zdravko.isPresent()) {
            User user = zdravko.get();
            if (user.getPassword().equals("test")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
        Optional<User> luka = userRepository.findByEmail("luka@gmail.com");
        if (luka.isPresent()) {
            User user = luka.get();
            if (user.getPassword().equals("test")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }

        log.info("Added all default users in the database");
    }

    public List<UserResponse> getUsersByRole(RoleEnum roleName) {
        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new GymManagmentException("Role with name " + roleName + " does not exist!"));

        return userRepository.findUsersByRole(role)
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().getName().name()))
                .toList();
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        JwtGenerationResult accessResult = jwtUtil.generateToken(loginRequest.getEmail(), "access");

        String accessToken = accessResult.getToken();
        Long accessExpiry = accessResult.getTimestamp();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        DashboardUserDetails userDetails = (DashboardUserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .findFirst().orElse(null);

        log.info("User login information: [Id: {}, Role: {}]", userDetails.getId(), role);

        return new JwtResponse(accessToken, accessExpiry,
                new UserResponse(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName(), userDetails.getEmail(), role));
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Integer addUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        var role = roleRepository.findByName(RoleEnum.KORISNIK).orElse(null);
        user.setRole(role);

        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByEmail(signupRequest.getEmail());
        if (savedUser.isPresent()) {
            return savedUser.get().getId();
        } else {
            throw new GymManagmentException("Spremanje korisnika sa email-om " + signupRequest.getEmail() + " nije bilo uspješno!");
        }
    }

    private Integer addUserInternal(SignupRequest signupRequest, RoleEnum role) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        var roleEntity = roleRepository.findByName(role).orElse(null);
        user.setRole(roleEntity);

        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByEmail(signupRequest.getEmail());
        return savedUser.map(User::getId).orElse(null);
    }

}
