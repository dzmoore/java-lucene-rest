package com.dylanmooresoftware.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dylanmooresoftware.model.Customer;
import com.dylanmooresoftware.service.SimilarityService;

@RestController("similarity")
public class SimilarityController {
  private static final Logger logger = Logger.getLogger(SimilarityController.class);
  
  @Autowired
  private SimilarityService similarityService;
  
  @RequestMapping("/similarity/similar-to/{pk}/{resultCount}")
  public List<Customer> customerSimilarToGet(@PathVariable(value="pk") final int pk, @PathVariable(value="resultCount") final int resultCount) {
    logger.debug("customerSimilarToGet: "+String.valueOf(pk));
    
    final List<Customer> similarCustomers = similarityService.findSimilarCustomers(pk, resultCount);
    
    if (similarCustomers == null) {
      throw new InternalServerErrorException();
    }
    
    return similarCustomers;
  }
}
