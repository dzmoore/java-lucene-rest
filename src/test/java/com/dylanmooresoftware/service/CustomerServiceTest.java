package com.dylanmooresoftware.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dylanmooresoftware.config.WebAppConfigurationAware;
import com.dylanmooresoftware.customer.Customer;
import com.dylanmooresoftware.service.impl.CustomerService;

import junit.framework.TestCase;

public class CustomerServiceTest extends WebAppConfigurationAware {
  @Autowired
  private CustomerService customerService;
  
  @Autowired
  private CustomerIndexService customerIndexService;
  
  @Test
  public void testFindSimilarCustomers() throws Exception {
    Customer cust = new Customer();
    cust.setBillingEmail("billing@email.com");
    cust.setEmail("email@email.com");
    cust.setFirstName("John");
    cust.setLastName("Johnson");
    cust.setPhoneNumber("(123) 456-8990");
    cust = customerService.addCustomer(cust);
    
    Customer cust1 = new Customer();
    cust1.setBillingEmail("milling@email.com");
    cust1.setEmail("email@email.com");
    cust1.setFirstName("John");
    cust1.setLastName("Johnson");
    cust1.setPhoneNumber("(123) 456-8990");
    cust1 = customerService.addCustomer(cust1);
    
    Customer cust2 = new Customer();
    cust2.setBillingEmail("illing@email.com");
    cust2.setEmail("jonnyj@email.com");
    cust2.setFirstName("Jon");
    cust2.setLastName("J");
    cust2.setPhoneNumber("(123) 456-8990");
    cust2 = customerService.addCustomer(cust1);
    
    Customer cust3 = new Customer();
    cust3.setBillingEmail("steve@steve-mail.com");
    cust3.setEmail("steve@gmail.com");
    cust3.setFirstName("Steve");
    cust3.setLastName(null);
    cust3.setPhoneNumber("(941) 444-8000");
    cust3 = customerService.addCustomer(cust3);
    
    customerIndexService.indexCustomers();
    
    final List<Customer> similarToCust = customerService.findSimilarCustomers(cust.getPk(), 4);
    Logger.getLogger(CustomerServiceTest.class).debug("similar: "+String.valueOf(similarToCust));
    
    final int custPk = cust.getPk();
    final int cust1Pk = cust1.getPk();
    final int cust2Pk = cust2.getPk();
    final int cust3Pk = cust3.getPk();
    similarToCust.forEach(ea -> {
      if (ea != null) {
        TestCase.assertFalse(ea.getPk() == cust3Pk);
        TestCase.assertFalse(ea.getPk() == custPk);
        TestCase.assertTrue(ea.getPk() == cust1Pk || ea.getPk() == cust2Pk);
      }
    });
  }
  
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
