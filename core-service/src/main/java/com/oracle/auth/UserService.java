package com.oracle.auth;

import com.oracle.entity.Customer;
import com.oracle.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, CustomerService customerService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
    }

    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        repository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists");
        });
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(request.getRole());

        // Try to attach or create a customer if provided
        String linkedCustomerId = resolveOrCreateCustomer(request);
        log.debug("Resolved customerId for username={}: {}", request.getUsername(), linkedCustomerId);
        if (linkedCustomerId != null && !linkedCustomerId.isBlank()) {
            user.setCustomerId(linkedCustomerId);
        }

        AppUser saved = repository.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getRoles(), saved.getCustomerId());
    }

    public AppUser findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = findByUsername(username);
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    private String resolveOrCreateCustomer(UserRegisterRequest request) {
        // Use explicit customerId if provided
        if (request.getCustomerId() != null && !request.getCustomerId().isBlank()) {
            return request.getCustomerId();
        }

        // Determine if we have enough info to create a customer
        UserRegisterRequest.CustomerDto nested = request.getCustomer();
        String firstName = nested != null && nested.getFirstName() != null ? nested.getFirstName() : request.getFirstName();
        String lastName = nested != null && nested.getLastName() != null ? nested.getLastName() : request.getLastName();
        String email = nested != null && nested.getEmail() != null ? nested.getEmail() : request.getEmail();
        String phone = nested != null && nested.getPhone() != null ? nested.getPhone() : request.getPhone();
        String dobStr = nested != null && nested.getDob() != null ? nested.getDob() : request.getDob();

        if (email == null && request.getUsername() != null && request.getUsername().contains("@")) {
            // Fallback: treat username as email if it looks like one
            email = request.getUsername();
        }

        if (firstName == null && lastName == null && email == null && phone == null && dobStr == null) {
            log.debug("No customer info provided in registration payload for username={}", request.getUsername());
            return null; // no customer info provided
        }

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);
        if (dobStr != null && !dobStr.isBlank()) {
            try { customer.setDob(LocalDate.parse(dobStr)); } catch (Exception ignored) {}
        }
        Customer created = customerService.create(customer);
        log.debug("Created customer id={} for username={}", created.getId(), request.getUsername());
        return created.getId();
    }
}
