package com.dylanmooresoftware.service;

import java.util.List;

import com.dylanmooresoftware.customer.Customer;
import com.dylanmooresoftware.http.UpdateResult;

public interface CustomerService {

  Customer getCustomer(int pk);

  UpdateResult updateCustomer(Customer customer);

  Customer addCustomer(Customer customer);

  UpdateResult deleteCustomer(int pk);

  List<Customer> findSimilarCustomers(int similarToCustomerPk, int resultCount);

}
