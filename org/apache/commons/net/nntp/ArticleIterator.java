package org.apache.commons.net.nntp;

import java.util.Iterator;

class ArticleIterator implements Iterator<Article>, Iterable<Article> {
  private final Iterator<String> stringIterator;
  
  public ArticleIterator(Iterable<String> iterableString) {
    this.stringIterator = iterableString.iterator();
  }
  
  public boolean hasNext() {
    return this.stringIterator.hasNext();
  }
  
  public Article next() {
    String line = this.stringIterator.next();
    return NNTPClient.__parseArticleEntry(line);
  }
  
  public void remove() {
    this.stringIterator.remove();
  }
  
  public Iterator<Article> iterator() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\ArticleIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */