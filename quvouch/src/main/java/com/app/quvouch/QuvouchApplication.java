package com.app.quvouch;

import com.app.quvouch.Models.Role;
import com.app.quvouch.config.AppConstant;
import com.app.quvouch.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class QuvouchApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(QuvouchApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        //admin role
        repository.findByRoleName("ROLE_"+ AppConstant.ADMIN_ROLE).ifPresentOrElse(role->{

        },()->{
            Role role = new Role();
            role.setRoleName("ROLE_"+AppConstant.ADMIN_ROLE);
            role.setId(UUID.randomUUID());
            repository.save(role);
        });

        //user role
        repository.findByRoleName("ROLE_"+ AppConstant.USER_ROLE).ifPresentOrElse(role->{

        },()->{
            Role role = new Role();
            role.setRoleName("ROLE_"+AppConstant.USER_ROLE);
            role.setId(UUID.randomUUID());
            repository.save(role);
        });
        //sale Representative
        repository.findByRoleName("ROLE_"+ AppConstant.SALE_REPRESENTATIVE).ifPresentOrElse(role->{

        },()->{
            Role role = new Role();
            role.setRoleName("ROLE_"+AppConstant.SALE_REPRESENTATIVE);
            role.setId(UUID.randomUUID());
            repository.save(role);
        });

        //clint role
        repository.findByRoleName("ROLE_"+ AppConstant.CLIENT_ROLE).ifPresentOrElse(role->{

        },()->{
            Role role = new Role();
            role.setRoleName("ROLE_"+AppConstant.CLIENT_ROLE);
            role.setId(UUID.randomUUID());
            repository.save(role);
        });
    }
}
