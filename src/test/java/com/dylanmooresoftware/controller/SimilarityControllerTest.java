package com.dylanmooresoftware.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dylanmooresoftware.config.WebAppConfigurationAware;
import com.dylanmooresoftware.service.CustomerIndexService;

public class SimilarityControllerTest extends WebAppConfigurationAware {
  @Autowired
  private CustomerIndexService customerIndexService;
  
  @Before
  public void setup() throws IOException {
    customerIndexService.indexCustomers();
  }
  
  @Test
  public void findSimilarCustomers() throws Exception {
    mockMvc.perform(get("/customer/similar-to/1/10"))
      .andExpect(status().isOk());
  }
}
