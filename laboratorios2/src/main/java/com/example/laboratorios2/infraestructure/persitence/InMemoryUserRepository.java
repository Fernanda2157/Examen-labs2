package com.example.laboratorios2.infraestructure.persitence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.laboratorios2.domain.model.User;
import com.example.laboratorios2.domain.repository.UserRepository;
@Repository
public class  InMemoryUserRepository implements UserRepository {
private final List<User> users = new ArrayList<>();
public InMemoryUserRepository() {
        users.add(new User(1, "Ana Torres", "estudiante", "1712345678", "Sistemas"));
        users.add(new User(2, "Luis Perez", "estudiante", "1798765432", "Software"));
        users.add(new User(3, "Marta Ruiz", "admin", "1700000001", "hardware"));
        users.add(new User(4, "Jorge Vaca", "admin", "1700000002", "software"));
        users.add(new User(5, "Sofia Leon", "admin", "1700000003", "redes"));
    }

    @Override
    public Optional<User> findById(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public Optional<User> findAdminByArea(String area) {
        return users.stream()
                .filter(u -> u.isAdmin() && u.getAreaOrProgram().equalsIgnoreCase(area))
                .findFirst();
    }
    
}
