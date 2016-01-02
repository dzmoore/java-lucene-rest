package com.dylanmooresoftware.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.dylanmooresoftware.customer.Customer;

@Component
public class CustomerDao {
  private static final Logger logger = Logger.getLogger(CustomerDao.class);
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  public Customer findCustomer(final int pk) {
    final List<Customer> customers = jdbcTemplate.query(
      "select " + 
      "  pk, " + 
      "  first_name, "+
      "  last_name, "+
      "  phone_number, "+
      "  email, " +
      "  billing_email " +
      "from customer "+
      "where pk = ?", 
      (rs, row) -> {
        final Customer customer = new Customer();
        customer.setPk(rs.getInt("pk"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setEmail(rs.getString("email"));
        customer.setBillingEmail(rs.getString("billing_email"));
        
        return customer;
      },
      pk
    );
    
    return customers.size() > 0 ? customers.get(0) : null;
  }

  public int updateCustomer(final Customer customer) {
    return jdbcTemplate.update(
      "update customer set "+
      "  first_name = ?, "+
      "  last_name = ?, "+
      "  phone_number = ?, "+
      "  email = ? ,"+
      "  billing_email = ? "+
      "where pk = ?",
      customer.getFirstName(),
      customer.getLastName(),
      customer.getPhoneNumber(),
      customer.getEmail(),
      customer.getBillingEmail(),
      customer.getPk()
    );
  }

  public Customer addCustomer(Customer customer) {
    final KeyHolder keyHolder = new GeneratedKeyHolder();
    
    final int result = jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(
        "insert into customer "+
        "  (first_name, last_name, phone_number, email, billing_email) values "+
        "  (?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, customer.getFirstName());
      ps.setString(2, customer.getLastName());
      ps.setString(3, customer.getPhoneNumber());
      ps.setString(4, customer.getEmail());
      ps.setString(5, customer.getBillingEmail());
      
      return ps;
    }, keyHolder);
  
    logger.debug("addCustomer inserted rows: " + String.valueOf(result));
    logger.debug("addCustomer keyHolder.getKey: " + String.valueOf(keyHolder.getKey()));
    
    final Customer newCustomer = new Customer(customer);
    
    newCustomer.setPk(keyHolder.getKey().intValue());
      
    return newCustomer;
  }

  public int deleteCustomer(int pk) {
    return jdbcTemplate.update(
        "delete from customer where pk = ?",
        pk
    );
  }
}
