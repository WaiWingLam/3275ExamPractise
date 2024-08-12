package org.example.exam;

import org.example.exam.entities.Customer;
import org.example.exam.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(new Customer(null, 115, "Jasper Diaz", 15000.0, 5, "Savings-Deluxe"));
            customerRepository.save(new Customer(null, 112, "Zanip Mendez", 5000.0, 2, "Savings-Deluxe"));
            customerRepository.save(new Customer(null, 113, "Geronima Esper", 6000.0, 5, "Savings-Regular"));
            customerRepository.findAll().forEach(p->{
                System.out.println(p.getName());
            });
        };
    }

}


