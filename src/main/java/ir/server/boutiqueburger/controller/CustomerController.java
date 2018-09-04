package ir.server.boutiqueburger.controller;

import ir.server.boutiqueburger.repository.CustomerRepository;
import ir.server.boutiqueburger.exception.ResourceNotFoundException;
import ir.server.boutiqueburger.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping("/all")
    public String getall() {
        return "ok";
    }

    // Get All customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Create a new customers
    //@PostMapping("/add")
    @PostMapping("/customer")
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PostMapping(path="/addcustomer") // Map ONLY GET Requests
    public @ResponseBody String addNewCustomer (@Valid @RequestBody Customer customer) {
        customerRepository.save(customer);
        return "Saved";
    }

    // Get a Single customers
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable(value = "id") Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", customerId));
    }

    // Update a customers
    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable(value = "id") Long customerId, @Valid @RequestBody Customer customerDetails) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer", "id", customerId));

        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setDescription(customerDetails.getDescription());

        Customer updatedCustomer = customerRepository.save(customer);
        return updatedCustomer;
    }

    // Delete a customers
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(value = "id") Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer", "id", customerId));

        customerRepository.delete(customer);

        return ResponseEntity.ok().build();
    }

}


