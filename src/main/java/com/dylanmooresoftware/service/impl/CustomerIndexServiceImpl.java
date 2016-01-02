package com.dylanmooresoftware.service.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dylanmooresoftware.service.CustomerIndexService;

@Component
public class CustomerIndexServiceImpl implements CustomerIndexService {
  private static final int INDEX_PAGE_SIZE = 1000;
  
  private static final Logger logger = Logger.getLogger(CustomerIndexServiceImpl.class);
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  @Autowired
  private Directory index; 
  
  @Autowired
  private Analyzer analyzer;
  
  @Transactional
  @Override
  public synchronized void indexCustomers() throws IOException {
    logger.debug("attempting customer re-indexing");
    
    final int customerCount = jdbcTemplate.queryForObject("select count(*) from customer", Integer.class);
    
    if (customerCount == 0) {
      logger.warn("customer count is 0! aborting indexing");
      return;
    }
    
    final IndexWriterConfig config = new IndexWriterConfig(analyzer);

    IndexWriter w = new IndexWriter(index, config);
    
    w.deleteAll();
    
    
    /* paginate populate the index so all customer rows aren't loaded into memory at once */
    int pageCount = (int)Math.ceil((double)customerCount/(double)INDEX_PAGE_SIZE);
    
    logger.debug(String.format("%1$s customer pages of size %2$s; %3$s total customer records found.",
        pageCount, INDEX_PAGE_SIZE, customerCount));
    
    IntStream.range(0, pageCount)
      .forEach(i -> {
        logger.debug("indexing customer page " + i);
        
        jdbcTemplate.query(
          "select limit ? ? * from customer", (rs, row) -> {
            final Document doc = new Document();
            doc.add(new IntField("pk", rs.getInt("pk"), Field.Store.YES));
           
            setTextFieldIfResultSetStringNotBlank(rs, "first_name", "firstName", doc);
            setTextFieldIfResultSetStringNotBlank(rs, "last_name", "lastName", doc);
            setTextFieldIfResultSetStringNotBlank(rs, "phone_number", "phoneNumber", doc);
            setTextFieldIfResultSetStringNotBlank(rs, "email", "email", doc);
            setTextFieldIfResultSetStringNotBlank(rs, "billing_email", "billingEmail", doc);
            
            final StringBuilder contentBuilder = new StringBuilder();
            
            contentBuilder
              .append(rs.getString("first_name"))
              .append(rs.getString("last_name"))
              .append(rs.getString("phone_number"))
              .append(rs.getString("email"))
              .append(rs.getString("billing_email"));
            
            doc.add(new TextField("content", contentBuilder.toString(), Field.Store.YES));
           
            try {
              w.addDocument(doc);
              
            } catch (Exception e) {
              logger.error("error occurred writing document to index for customer pk " + rs.getInt("pk"), e);
            }
            
            return null;
          },
          i * INDEX_PAGE_SIZE,
          (i+1) * INDEX_PAGE_SIZE
        );
      });

    w.close();
  }
  
  private void setTextFieldIfResultSetStringNotBlank(final ResultSet rs, final String colName, final String fieldName, final Document doc) throws SQLException {
    final String str = rs.getString(colName);
    if (StringUtils.isNotBlank(str)) {
      doc.add(new TextField(fieldName, str, Field.Store.YES));
    }
  }
}
