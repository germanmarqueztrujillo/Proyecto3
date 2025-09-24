package com.example.ecommerce.config;

import com.example.ecommerce.customer.model.Customer;
import com.example.ecommerce.customer.repository.CustomerRepository;
import com.example.ecommerce.product.model.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.order.model.Status;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DataLoader(CustomerRepository customerRepository,
                      ProductRepository productRepository,
                      OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        // ---- Customers ----
        Customer alice = new Customer();
        alice.setName("Alice");
        alice.setEmail("alice@example.com");
        customerRepository.save(alice);

        Customer bob = new Customer();
        bob.setName("Bob");
        bob.setEmail("bob@example.com");
        customerRepository.save(bob);

        // ---- Products ----
        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setPrice(new BigDecimal("1200.00"));
        productRepository.save(laptop);

        Product phone = new Product();
        phone.setName("Smartphone");
        phone.setPrice(new BigDecimal("800.00"));
        productRepository.save(phone);

        Product headphones = new Product();
        headphones.setName("Headphones");
        headphones.setPrice(new BigDecimal("150.00"));
        productRepository.save(headphones);

        // ---- Orders ----
        Order order1 = new Order();
        order1.setCustomer(alice);
        order1.setProducts(List.of(laptop, headphones));
        order1.setStatus(Status.CREATED);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setCustomer(bob);
        order2.setProducts(List.of(phone));
        order2.setStatus(Status.PAID);
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setCustomer(alice);
        order3.setProducts(List.of(laptop, phone, headphones));
        order3.setStatus(Status.SHIPPED);
        orderRepository.save(order3);
    }
}
