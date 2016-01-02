package com.dylanmooresoftware.dao;

import com.dylanmooresoftware.customer.Customer;

public interface CustomerDao {

  Customer findCustomer(int pk);

  int updateCustomer(Customer customer);

  Customer addCustomer(Customer customer);

  int deleteCustomer(int pk);

}
