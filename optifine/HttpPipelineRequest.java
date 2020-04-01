package optifine;

public class HttpPipelineRequest {
  private HttpRequest httpRequest = null;
  
  private HttpListener httpListener = null;
  
  private boolean closed = false;
  
  public HttpPipelineRequest(HttpRequest p_i58_1_, HttpListener p_i58_2_) {
    this.httpRequest = p_i58_1_;
    this.httpListener = p_i58_2_;
  }
  
  public HttpRequest getHttpRequest() {
    return this.httpRequest;
  }
  
  public HttpListener getHttpListener() {
    return this.httpListener;
  }
  
  public boolean isClosed() {
    return this.closed;
  }
  
  public void setClosed(boolean p_setClosed_1_) {
    this.closed = p_setClosed_1_;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\HttpPipelineRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */