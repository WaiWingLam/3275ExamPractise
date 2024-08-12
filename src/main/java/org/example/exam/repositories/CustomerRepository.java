package org.example.exam.repositories;

import org.example.exam.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findCustomerById (long kw);
    List<Customer> findCustomerByCustomerId (int id);
    List<Customer> findCustomerByName (String name);
}
