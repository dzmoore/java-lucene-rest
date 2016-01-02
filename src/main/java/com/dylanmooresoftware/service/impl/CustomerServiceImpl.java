/**
 * 
 */
package com.dylanmooresoftware.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dylanmooresoftware.customer.Customer;
import com.dylanmooresoftware.dao.CustomerDao;
import com.dylanmooresoftware.http.UpdateResult;
import com.dylanmooresoftware.service.CustomerService;

@Component
public class CustomerServiceImpl implements CustomerService {
  private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class);

  @Autowired
  private CustomerDao customerDao;

  @Autowired
  private Analyzer analyzer;

  @Autowired
  private Directory index;

  @Override
  public List<Customer> findSimilarCustomers(final int similarToCustomerPk, final int resultCount) {
    /* find the customer in question */
    final Customer customer = customerDao.findCustomer(similarToCustomerPk);

    final List<Customer> similarCustomers = new ArrayList<>();

    // create lucene query
    final String queryStr = createSimilarCustomerLuceneQuery(customer);

    logger.debug("lucene query: " + queryStr);

    Query q;
    try {
      q = new QueryParser("email", analyzer).parse(queryStr);
      
    } catch (ParseException e) {
      logger.error("query parse error", e);
      return Collections.emptyList();
    }

    // search for similar customers
    IndexReader reader;
    try {
      reader = DirectoryReader.open(index);
      
    } catch (IOException e) {
      logger.error("error opening index reader", e);
      return Collections.emptyList();
    }

    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(resultCount);
    try {
      searcher.search(q, collector);
      
    } catch (IOException e) {
      logger.error("error querying customer index", e);
      return Collections.emptyList();
    }
    
    ScoreDoc[] hits = collector.topDocs().scoreDocs;

    Arrays.asList(hits).forEach(hit -> {
      try {
        final Document doc = searcher.doc(hit.doc);
        
        final Customer cust = new Customer();
        final int pk = doc.getField("pk").numericValue().intValue();
        
        if (pk == customer.getPk()) {
          return;
        }
        
        cust.setPk(pk);
        cust.setBillingEmail(doc.get("billingEmail"));
        cust.setFirstName(doc.get("firstName"));
        cust.setLastName(doc.get("lastName"));
        cust.setPhoneNumber(doc.get("phoneNumber"));
        cust.setEmail(doc.get("email"));

        similarCustomers.add(cust);

      } catch (Exception e) {
        logger.error("error occurred while grabbing doc from search hit", e);
      }
    });

    try {
      reader.close();
      
    } catch (IOException e) {
      logger.error("error closing index reader", e);
    }

    return similarCustomers;
  }

  private static void appendLuceneQueryTerm(final StringBuilder queryBuilder, final String name, final String value) {
    if (StringUtils.isNotBlank(value)) {
      if (queryBuilder.length() > 0) {
        queryBuilder.append(" OR ");
      }

      queryBuilder.append(name).append(": \"").append(value).append("\"~");
    }
  }

  private static String createSimilarCustomerLuceneQuery(final Customer customer) {
    final StringBuilder custQryStrBldr = new StringBuilder();

    appendLuceneQueryTerm(custQryStrBldr, "firstName", customer.getFirstName());
    appendLuceneQueryTerm(custQryStrBldr, "lastName", customer.getLastName());
    appendLuceneQueryTerm(custQryStrBldr, "email", customer.getEmail());
    appendLuceneQueryTerm(custQryStrBldr, "billingEmail", customer.getBillingEmail());
    appendLuceneQueryTerm(custQryStrBldr, "phoneNumber", customer.getPhoneNumber());

    if (custQryStrBldr.length() > 0) {
      custQryStrBldr.append(" AND ");
    }

    custQryStrBldr.append("NOT pk: \"").append(customer.getPk()).append("\"");

    return custQryStrBldr.toString();
  }

  @Override
  public Customer getCustomer(final int pk) {
    return customerDao.findCustomer(pk);
  }

  @Override
  public UpdateResult updateCustomer(final Customer customer) {
    final int result = customerDao.updateCustomer(customer);

    return result > 0 ? new UpdateResult("Update successful.") : new UpdateResult("Update failed.");
  }

  @Override
  public Customer addCustomer(Customer customer) {
    return customerDao.addCustomer(customer);
  }

  @Override
  public UpdateResult deleteCustomer(int pk) {
    final int result = customerDao.deleteCustomer(pk);

    return result > 0 ? new UpdateResult("Delete successful.") : new UpdateResult("Delete failed.");
  }
}
