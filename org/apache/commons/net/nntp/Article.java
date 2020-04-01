package org.apache.commons.net.nntp;

import java.io.PrintStream;
import java.util.ArrayList;

public class Article implements Threadable {
  private long articleNumber;
  
  private String subject;
  
  private String date;
  
  private String articleId;
  
  private String simplifiedSubject;
  
  private String from;
  
  private ArrayList<String> references;
  
  private boolean isReply = false;
  
  public Article kid;
  
  public Article next;
  
  public Article() {
    this.articleNumber = -1L;
  }
  
  public void addReference(String msgId) {
    if (msgId == null || msgId.length() == 0)
      return; 
    if (this.references == null)
      this.references = new ArrayList<>(); 
    this.isReply = true;
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = msgId.split(" ")).length, b = 0; b < i; ) {
      String s = arrayOfString[b];
      this.references.add(s);
      b++;
    } 
  }
  
  public String[] getReferences() {
    if (this.references == null)
      return new String[0]; 
    return this.references.<String>toArray(new String[this.references.size()]);
  }
  
  private void simplifySubject() {
    int start = 0;
    String subject = getSubject();
    int len = subject.length();
    boolean done = false;
    while (!done) {
      done = true;
      while (start < len && subject.charAt(start) == ' ')
        start++; 
      if (start < len - 2 && (
        subject.charAt(start) == 'r' || subject.charAt(start) == 'R') && (
        subject.charAt(start + 1) == 'e' || subject.charAt(start + 1) == 'E'))
        if (subject.charAt(start + 2) == ':') {
          start += 3;
          done = false;
        } else if (start < len - 2) {
          if (subject.charAt(start + 2) == '[' || subject.charAt(start + 2) == '(') {
            int i = start + 3;
            while (i < len && subject.charAt(i) >= '0' && subject.charAt(i) <= '9')
              i++; 
            if (i < len - 1 && (
              subject.charAt(i) == ']' || subject.charAt(i) == ')') && 
              subject.charAt(i + 1) == ':') {
              start = i + 2;
              done = false;
            } 
          } 
        }  
      if ("(no subject)".equals(this.simplifiedSubject))
        this.simplifiedSubject = ""; 
      int end = len;
      while (end > start && subject.charAt(end - 1) < ' ')
        end--; 
      if (start == 0 && end == len) {
        this.simplifiedSubject = subject;
        continue;
      } 
      this.simplifiedSubject = subject.substring(start, end);
    } 
  }
  
  public static void printThread(Article article) {
    printThread(article, 0, System.out);
  }
  
  public static void printThread(Article article, PrintStream ps) {
    printThread(article, 0, ps);
  }
  
  public static void printThread(Article article, int depth) {
    printThread(article, depth, System.out);
  }
  
  public static void printThread(Article article, int depth, PrintStream ps) {
    for (int i = 0; i < depth; i++)
      ps.print("==>"); 
    ps.println(String.valueOf(article.getSubject()) + "\t" + article.getFrom() + "\t" + article.getArticleId());
    if (article.kid != null)
      printThread(article.kid, depth + 1); 
    if (article.next != null)
      printThread(article.next, depth); 
  }
  
  public String getArticleId() {
    return this.articleId;
  }
  
  public long getArticleNumberLong() {
    return this.articleNumber;
  }
  
  public String getDate() {
    return this.date;
  }
  
  public String getFrom() {
    return this.from;
  }
  
  public String getSubject() {
    return this.subject;
  }
  
  public void setArticleId(String string) {
    this.articleId = string;
  }
  
  public void setArticleNumber(long l) {
    this.articleNumber = l;
  }
  
  public void setDate(String string) {
    this.date = string;
  }
  
  public void setFrom(String string) {
    this.from = string;
  }
  
  public void setSubject(String string) {
    this.subject = string;
  }
  
  public boolean isDummy() {
    return (this.articleNumber == -1L);
  }
  
  public String messageThreadId() {
    return this.articleId;
  }
  
  public String[] messageThreadReferences() {
    return getReferences();
  }
  
  public String simplifiedSubject() {
    if (this.simplifiedSubject == null)
      simplifySubject(); 
    return this.simplifiedSubject;
  }
  
  public boolean subjectIsReply() {
    return this.isReply;
  }
  
  public void setChild(Threadable child) {
    this.kid = (Article)child;
    flushSubjectCache();
  }
  
  private void flushSubjectCache() {
    this.simplifiedSubject = null;
  }
  
  public void setNext(Threadable next) {
    this.next = (Article)next;
    flushSubjectCache();
  }
  
  public Threadable makeDummy() {
    return new Article();
  }
  
  public String toString() {
    return String.valueOf(this.articleNumber) + " " + this.articleId + " " + this.subject;
  }
  
  @Deprecated
  public int getArticleNumber() {
    return (int)this.articleNumber;
  }
  
  @Deprecated
  public void setArticleNumber(int a) {
    this.articleNumber = a;
  }
  
  @Deprecated
  public void addHeaderField(String name, String val) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\Article.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */