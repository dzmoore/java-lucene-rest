package com.dylanmooresoftware.http;

public class UpdateResult {
  private String result;

  public UpdateResult() {
    super();
  }
  
  public UpdateResult(final String result) {
    this();
    this.result = result;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("UpdateResult [result=");
    builder.append(result);
    builder.append("]");
    return builder.toString();
  }
}
