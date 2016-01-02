package com.dylanmooresoftware.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dylanmooresoftware.config.WebAppConfigurationAware;
import com.dylanmooresoftware.model.Customer;

import junit.framework.TestCase;

public class CustomerServiceTest extends WebAppConfigurationAware {
  @Autowired
  private CustomerService customerService;
  
  @Test
  public void testAddCustomer() {
    final Customer customer = new Customer();
    customer.setFirstName("John");
    
    final Customer newCustomer = customerService.addCustomer(customer);
    
    TestCase.assertNotNull(newCustomer);
    TestCase.assertTrue(newCustomer.getPk() >= 0);
    TestCase.assertTrue(StringUtils.equals(newCustomer.getFirstName(), "John"));
  }
  
  @Test
  public void testDeleteCustomer() {
    final Customer customer = new Customer();
    customer.setFirstName("John");
    
    final Customer newCustomer = customerService.addCustomer(customer);
    
    TestCase.assertNotNull(customerService.getCustomer(newCustomer.getPk()));
    
    TestCase.assertNotNull(customerService.deleteCustomer(newCustomer.getPk()).getResult());
    
    TestCase.assertNull(customerService.getCustomer(newCustomer.getPk()));
    
  }
  
  @Test
  public void testUpdateCustomer() {
    final Customer customer = new Customer();
    customer.setFirstName("John");
    
    final Customer newCustomer = customerService.addCustomer(customer);
    
    TestCase.assertNotNull(customerService.getCustomer(newCustomer.getPk()));
   
    final Customer modifiedCustomer = new Customer(newCustomer);
    modifiedCustomer.setFirstName("Jane");
    TestCase.assertNotNull(customerService.updateCustomer(modifiedCustomer));
    
    TestCase.assertTrue(StringUtils.equals(customerService.getCustomer(newCustomer.getPk()).getFirstName(), "Jane"));
  }
  
}
