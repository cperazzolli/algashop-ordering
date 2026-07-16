package com.algaworks.algashop.ordering.presentation;

import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.algaworks.algashop.ordering.application.customer.query.CustomerFilter;
import com.algaworks.algashop.ordering.application.customer.query.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.query.CustomerQueryService;
import com.algaworks.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManagementApplicationService customerManagementApplicationService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody @Valid CustomerInput input, HttpServletResponse httpServletResponse) {
        UUID customerId = customerManagementApplicationService.create(input);

        UriComponentsBuilder builder = MvcUriComponentsBuilder.fromMethodCall(
                MvcUriComponentsBuilder.on(CustomerController.class).findById(customerId)
        );

        httpServletResponse.addHeader("Location", builder.toUriString());
        return customerQueryService.findById(customerId);
    }

    @GetMapping
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter customerFilter) {
        return PageModel.of(customerQueryService.filter(customerFilter));
    }

    @GetMapping("/{customerId}")
    public CustomerOutput findById(@PathVariable UUID customerId) {
      return customerQueryService.findById(customerId);
    }

    @DeleteMapping("{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID customerId) {
        customerManagementApplicationService.archive(customerId);
    }

    @PutMapping("{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerOutput update(@PathVariable UUID customerId,@RequestBody @Valid CustomerInput  customerInput) {
        CustomerUpdateInput customerUpdateInput = CustomerUpdateInput.builder()
                .firstName(customerInput.getFirstName())
                .lastName(customerInput.getLastName())
                .phone(customerInput.getPhone())
                .promotionNotificationsAllowed(customerInput.getPromotionNotificationsAllowed())
                .address(customerInput.getAddress())
                .build();

        customerManagementApplicationService.update(customerId,customerUpdateInput);
        return customerQueryService.findById(customerId);
    }
}
