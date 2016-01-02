package com.dylanmooresoftware.customer;

public class Customer {
  private int pk;
  private String firstName;
  private String lastName;
  private String email;
  private String billingEmail;
  private String phoneNumber;
  
  public Customer() {
    super();
    
    pk = -1;
  }
  
  public Customer(final Customer cust) {
    this();
    
    if (cust == null) {
      return;
    }
    
    this.pk = cust.pk;
    this.firstName = cust.firstName;
    this.lastName = cust.lastName;
    this.email = cust.email;
    this.billingEmail = cust.billingEmail;
    this.phoneNumber = cust.phoneNumber;
  }

  public int getPk() {
    return pk;
  }

  public void setPk(int pk) {
    this.pk = pk;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBillingEmail() {
    return billingEmail;
  }

  public void setBillingEmail(String billingEmail) {
    this.billingEmail = billingEmail;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Customer [pk=");
    builder.append(pk);
    builder.append(", firstName=");
    builder.append(firstName);
    builder.append(", lastName=");
    builder.append(lastName);
    builder.append(", email=");
    builder.append(email);
    builder.append(", billingEmail=");
    builder.append(billingEmail);
    builder.append(", phoneNumber=");
    builder.append(phoneNumber);
    builder.append("]");
    return builder.toString();
  }

  
  
  
}
