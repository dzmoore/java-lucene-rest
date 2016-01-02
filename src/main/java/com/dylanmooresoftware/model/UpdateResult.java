package com.dylanmooresoftware.model;

public class UpdateResult {
  private String result;
  private boolean error;

  public UpdateResult() {
    super();
    error = false;
  }
  
  public UpdateResult(final String result) {
    this();
    this.result = result;
  }
  
  public UpdateResult(final String result, final boolean error) {
    this();
    this.result = result;
    this.error = error;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }

  @Override
  public String toString() {
    return "UpdateResult [result=" + result + ", error=" + error + "]";
  }
  
  
}
