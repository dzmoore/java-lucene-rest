package com.dylanmooresoftware.customer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dylanmooresoftware.http.UpdateResult;
import com.dylanmooresoftware.service.CustomerService;

@RestController("customer")
public class CustomerController {
  private static final Logger logger = Logger.getLogger(CustomerController.class);
  
  @Autowired
  private CustomerService customerService;
  
  @RequestMapping("/customer/{pk}")
  public Customer customerGet(@PathVariable(value="pk") final int pk) {
    logger.debug("customerGet: "+String.valueOf(pk));
    
    return customerService.getCustomer(pk);
  }
  
  @RequestMapping(value = "/customer", method = RequestMethod.POST)
  public Customer customerPost(@RequestBody final Customer customer) {
    logger.debug("customerPost: "+String.valueOf(customer));
    
    return customerService.addCustomer(customer);
  }
  
  @RequestMapping(value = "/customer", method = RequestMethod.PUT)
  public UpdateResult customerPut(@RequestBody final Customer customer) {
    logger.debug("customerPut: "+String.valueOf(customer));
   
    return customerService.updateCustomer(customer);
  }
  
  @RequestMapping(value = "/customer/{pk}", method = RequestMethod.DELETE)
  public UpdateResult customerDelete(@PathVariable(value="pk") final int pk) {
    logger.debug("customerDelete: "+String.valueOf(pk));
   
    return customerService.deleteCustomer(pk);
  }
}
