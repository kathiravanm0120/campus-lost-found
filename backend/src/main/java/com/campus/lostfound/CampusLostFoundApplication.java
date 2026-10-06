package com.campus.lostfound;

import com.campus.lostfound.entity.Category;
import com.campus.lostfound.entity.Role;
import com.campus.lostfound.entity.User;
import com.campus.lostfound.repository.CategoryRepository;
import com.campus.lostfound.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CampusLostFoundApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusLostFoundApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(UserRepository users, CategoryRepository categories,
                               PasswordEncoder encoder) {
        return args -> {
            String[] names = {"Electronics", "Documents", "Books", "Clothing",
                    "Bags", "Keys", "Accessories", "Other"};
            for (String name : names) {
                if (categories.findByNameIgnoreCase(name).isEmpty()) {
                    categories.save(new Category(null, name));
                }
            }

            if (users.findByEmailIgnoreCase("admin@campus.local").isEmpty()) {
                User admin = new User();
                admin.setName("Campus Admin");
                admin.setEmail("admin@campus.local");
                admin.setPassword(encoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);
                users.save(admin);
            }
        };
    }
}
