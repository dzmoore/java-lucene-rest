package com.dylanmooresoftware.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dylanmooresoftware.model.UpdateResult;
import com.dylanmooresoftware.service.CustomerIndexService;

@RestController("index")
public class CustomerIndexController {
  private static final Logger logger = Logger.getLogger(CustomerIndexController.class);
  
  @Autowired
  private CustomerIndexService customerIndexService;
  
  @RequestMapping("/index/customers")
  public UpdateResult indexCustomersGet() {
    logger.debug("indexCustomers");
   
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          customerIndexService.indexCustomers();
        } catch (IOException e) {
          logger.error("error occurred while indexing customers", e);
        }
      }
    }).start();
    
    return new UpdateResult("Customer index started.");
  }
}
