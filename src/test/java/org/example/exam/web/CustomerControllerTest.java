package org.example.exam.web;

import org.example.exam.entities.Customer;
import org.example.exam.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CustomerControllerTest {

    Customer customer;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    View mockView;

    @InjectMocks
    CustomerController customerController;

    @BeforeEach
    void setUp() throws ParseException {

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John");

        customer.setDeposit(10000);
        customer.setYears(5);
        customer.setType("Savings-Regular");

        MockitoAnnotations.openMocks(this);

        mockMvc = standaloneSetup(customerController).setSingleView(mockView).build();
    }

    @Test
    public void findAll_ListView() throws Exception {
        List<Customer> list = new ArrayList<Customer>();
        list.add(customer);
        list.add(customer);

        when(customerRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listCustomers", list))
                .andExpect(view().name("customers"))
                .andExpect(model().attribute("listCustomers", hasSize(2)));

        verify(customerRepository,times(1)).findAll();
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void findByName() throws Exception {
        List<Customer> list = new ArrayList<>();
        list.add(customer);

        when(customerRepository.findCustomerByName("John")).thenReturn(list);

        mockMvc.perform(get("/index").param("keyword","John"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listCustomers", list))
                .andExpect(view().name("customers"))
                .andExpect(model().attribute("listCustomers", hasSize(1)));

        verify(customerRepository,times(1)).findCustomerByName(anyString());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void delete() {
        ArgumentCaptor<Long> idCapture = ArgumentCaptor.forClass(Long.class);
        doNothing().when(customerRepository).deleteById(idCapture.capture());
        customerRepository.deleteById(1L);
        assertEquals(1L, idCapture.getValue());
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void addCustomers() throws Exception {
        mockMvc.perform(get("/addCustomers"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", new Customer()))
                .andExpect(view().name("addCustomers"));
    }

    @Test
    void add() {
        when(customerRepository.save(customer)).thenReturn(customer);
        customerRepository.save(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void save() {
        when(customerRepository.save(customer)).thenReturn(customer);
        customerRepository.save(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void editCustomers() throws Exception {
        Customer s2 = new Customer();
        s2.setId(1L);
        s2.setName("John Mast");

        customer.setDeposit(11000);
        customer.setYears(4);
        customer.setType("Savings-Regular");

        Long lid = 1L;

        when(customerRepository.findById(lid)).thenReturn(Optional.of(s2));

        mockMvc.perform(get("/editCustomers").param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", s2))
                .andExpect(view().name("editCustomers"));

        verify(customerRepository,times(1)).findById(anyLong());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void customer2() {
    }

    @Test
    void customer() {
    }

    @Test
    void projectInvestments() {
    }
}