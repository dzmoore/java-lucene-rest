package com.dylanmooresoftware.customer;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.dylanmooresoftware.config.WebAppConfigurationAware;
import com.dylanmooresoftware.service.CustomerIndexService;
import com.google.gson.Gson;

public class CustomerControllerTest extends WebAppConfigurationAware {
  @Autowired
  private CustomerIndexService customerIndexService;
  
  @Before
  public void setup() throws IOException {
    customerIndexService.indexCustomers();
  }
  
  @Test
  public void customerGet() throws Exception {
    mockMvc.perform(get("/customer/1"))
      .andExpect(status().isOk())
      .andExpect(content().string(
        allOf(
          containsString("\"pk\":1"))
    ));
  }
  
  @Test
  public void findSimilarCustomers() throws Exception {
    mockMvc.perform(get("/customer/similar-to/1/10"))
      .andExpect(status().isOk());
  }
  
  @Test
  public void customerDelete() throws Exception {
    mockMvc.perform(delete("/customer/1"))
      .andExpect(status().isOk());
  }
  
  @Test
  public void customerPut() throws Exception {
    mockMvc.perform(put("/customer")
      .contentType(MediaType.APPLICATION_JSON)
      .content(new Gson().toJson(new Customer()))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
  
  @Test
  public void customerPost() throws Exception {
    mockMvc.perform(post("/customer")
      .contentType(MediaType.APPLICATION_JSON)
      .content(new Gson().toJson(new Customer()))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
  
}
