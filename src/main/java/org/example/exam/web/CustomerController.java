package org.example.exam.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.exam.entities.Customer;
import org.example.exam.entities.Investment;
import org.example.exam.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SessionAttributes({"a","e"})
@Controller
@AllArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    static int num=0;

    @GetMapping(path = "/")
    public String customer2(Model model, ModelMap mm, @RequestParam(name="keyword",defaultValue = "") String keyword, HttpSession session){
        List<Customer> customers;
        if (keyword.isEmpty()) {
            customers = customerRepository.findAll();
        } else {
            mm.put("e", 0);
            mm.put("a", 0);
            int key = Integer.parseInt(keyword);
            customers = customerRepository.findCustomerByCustomerId(key);
        }
        model.addAttribute("listCustomers", customers);
        return "customers";
    }

    @GetMapping(path="/index")
    public String customer(Model model,  @RequestParam(name="keyword",defaultValue = "")String keyword) {

        List<Customer> customers;
        if(keyword.isEmpty()) {
            customers = customerRepository.findAll();
        } else {
            customers = customerRepository.findCustomerByName(keyword);
        }
        model.addAttribute("listCustomers",customers);

        return "customers";
    }

    @GetMapping("/delete")
    public String delete(Long id) {
        customerRepository.deleteById(id);
        return "redirect:/index";
    }

    @GetMapping("/addCustomers")
    public String addCustomers(Model model) {
        model.addAttribute("customer", new Customer());
        return "addCustomers";
    }

    @PostMapping("/add")
    public String add(Model model, Customer customer, BindingResult bindingResult, ModelMap mm, HttpSession session, RedirectAttributes redirectAttributes) {

        if (customerRepository.findCustomerByCustomerId(customer.getCustomerId()) == null) {
            redirectAttributes.addFlashAttribute("err", "Customer already exists. Choose another number.");
            return "redirect:index";
        } else {
            customerRepository.save(customer);

            mm.put("e", 0);
            mm.put("a", 1);

            return "redirect:index";
        }
    }

    @PostMapping("/save")
    public String save(Model model, Customer customer, BindingResult bindingResult, ModelMap mm, HttpSession session, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "addCustomers";
        } else {
            customerRepository.save(customer);
            if (num == 2) {
                mm.put("e", 2);
                mm.put("a", 0);
            } else {
                mm.put("a", 1);
                mm.put("e", 0);
            }
            return "redirect:index";

        }
    }

    @GetMapping("/editCustomers")
    public String editCustomers(Model model, Long id, HttpSession session) {
        num = 2;
        session.setAttribute("info", 0);
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) throw new RuntimeException("Customer does not exist");
        model.addAttribute("customer", customer);
        return "editCustomers";
    }

    @GetMapping("/projectInvestment")
    public String projectInvestments(Model model, Long id, HttpSession session, ModelMap mm) {

        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) throw new RuntimeException("Customer does not exist");

        double init = customer.getDeposit();
        List<Investment> investmentList = new ArrayList<>();

        if(customer.getType().equals("Savings-Deluxe")) {
            for (int i = 1; i <= customer.getYears(); i++){

                double interest = init * 0.15;
                DecimalFormat df = new DecimalFormat("#.##");
                double fInterest = Double.parseDouble(df.format(interest));

                double ending = init + fInterest;

                double fEnding = Double.parseDouble(df.format(ending));

                Investment row = new Investment(null, i, init, fInterest, fEnding);

                investmentList.add(row);

                init = fEnding;
            }
        } else {
            for (int i = 1; i <= customer.getYears(); i++){

                double interest = init * 0.1;
                DecimalFormat df = new DecimalFormat("#.##");
                double fInterest = Double.parseDouble(df.format(interest));

                double ending = init + fInterest;

                double fEnding = Double.parseDouble(df.format(ending));

                Investment row = new Investment(null, i, init, fInterest, fEnding);

                investmentList.add(row);

                init = fEnding;
            }
        }

        mm.put("e", 0);
        mm.put("a", 0);

        model.addAttribute("customer", customer);
        model.addAttribute("investmentList", investmentList);
        return "projectInvestment";

    }
}
