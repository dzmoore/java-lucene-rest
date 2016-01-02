package com.dylanmooresoftware.service;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dylanmooresoftware.config.WebAppConfigurationAware;

import junit.framework.TestCase;

public class CustomerIndexServiceTest extends WebAppConfigurationAware {
  @Autowired
  private CustomerIndexService customerIndexService;
  
  @Autowired
  private Directory index; 
  
  @Autowired
  private Analyzer analyzer;
  
  @Test
  public void testIndexCustomers() throws IOException, ParseException {
    customerIndexService.indexCustomers();
    
    final Query qry = new QueryParser("email", analyzer).parse("email: \"sed@penatibuset.co.uk\"");
    IndexReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(10);
    searcher.search(qry, collector);
    ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
    TestCase.assertNotNull("Search hits was null.", hits);
    TestCase.assertTrue("Search hits was empty.", hits.length > 0);
  }
}
