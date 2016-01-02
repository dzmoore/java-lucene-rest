/**
 * 
 */
package com.dylanmooresoftware.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dylanmooresoftware.dao.CustomerDao;
import com.dylanmooresoftware.model.Customer;
import com.dylanmooresoftware.model.UpdateResult;

@Component
public class CustomerService {
  private static final Logger logger = Logger.getLogger(CustomerService.class);

  @Autowired
  private CustomerDao customerDao;

  public Customer getCustomer(final int pk) {
    return customerDao.findCustomer(pk);
  }

  public UpdateResult updateCustomer(final Customer customer) {
    final int result = customerDao.updateCustomer(customer);

    return result > 0 ? new UpdateResult("Update successful.") : new UpdateResult("Update failed.");
  }

  public Customer addCustomer(Customer customer) {
    return customerDao.addCustomer(customer);
  }

  public UpdateResult deleteCustomer(int pk) {
    final int result = customerDao.deleteCustomer(pk);

    return result > 0 ? new UpdateResult("Delete successful.") : new UpdateResult("Delete failed.");
  }
}
