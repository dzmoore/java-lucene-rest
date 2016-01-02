package com.dylanmooresoftware.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LuceneConfig {

  @Bean
  public Analyzer configureLuceneAnalyzer() {
    return new StandardAnalyzer();
  }
  
  @Bean
  public Directory configureLuceneDirectory() {
    return new RAMDirectory();
  }
}
