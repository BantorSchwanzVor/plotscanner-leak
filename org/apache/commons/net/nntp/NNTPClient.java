package org.apache.commons.net.nntp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.io.DotTerminatedMessageReader;
import org.apache.commons.net.io.DotTerminatedMessageWriter;
import org.apache.commons.net.io.Util;

public class NNTPClient extends NNTP {
  private void __parseArticlePointer(String reply, ArticleInfo pointer) throws MalformedServerReplyException {
    String[] tokens = reply.split(" ");
    if (tokens.length >= 3) {
      int i = 1;
      try {
        pointer.articleNumber = Long.parseLong(tokens[i++]);
        pointer.articleId = tokens[i++];
        return;
      } catch (NumberFormatException numberFormatException) {}
    } 
    throw new MalformedServerReplyException(
        "Could not parse article pointer.\nServer reply: " + reply);
  }
  
  private static void __parseGroupReply(String reply, NewsgroupInfo info) throws MalformedServerReplyException {
    String[] tokens = reply.split(" ");
    if (tokens.length >= 5) {
      int i = 1;
      try {
        info._setArticleCount(Long.parseLong(tokens[i++]));
        info._setFirstArticle(Long.parseLong(tokens[i++]));
        info._setLastArticle(Long.parseLong(tokens[i++]));
        info._setNewsgroup(tokens[i++]);
        info._setPostingPermission(0);
        return;
      } catch (NumberFormatException numberFormatException) {}
    } 
    throw new MalformedServerReplyException(
        "Could not parse newsgroup info.\nServer reply: " + reply);
  }
  
  static NewsgroupInfo __parseNewsgroupListEntry(String entry) {
    String[] tokens = entry.split(" ");
    if (tokens.length < 4)
      return null; 
    NewsgroupInfo result = new NewsgroupInfo();
    int i = 0;
    result._setNewsgroup(tokens[i++]);
    try {
      long lastNum = Long.parseLong(tokens[i++]);
      long firstNum = Long.parseLong(tokens[i++]);
      result._setFirstArticle(firstNum);
      result._setLastArticle(lastNum);
      if (firstNum == 0L && lastNum == 0L) {
        result._setArticleCount(0L);
      } else {
        result._setArticleCount(lastNum - firstNum + 1L);
      } 
    } catch (NumberFormatException e) {
      return null;
    } 
    switch (tokens[i++].charAt(0)) {
      case 'Y':
      case 'y':
        result._setPostingPermission(
            2);
        return result;
      case 'N':
      case 'n':
        result._setPostingPermission(3);
        return result;
      case 'M':
      case 'm':
        result._setPostingPermission(1);
        return result;
    } 
    result._setPostingPermission(0);
    return result;
  }
  
  static Article __parseArticleEntry(String line) {
    Article article = new Article();
    article.setSubject(line);
    String[] parts = line.split("\t");
    if (parts.length > 6) {
      int i = 0;
      try {
        article.setArticleNumber(Long.parseLong(parts[i++]));
        article.setSubject(parts[i++]);
        article.setFrom(parts[i++]);
        article.setDate(parts[i++]);
        article.setArticleId(parts[i++]);
        article.addReference(parts[i++]);
      } catch (NumberFormatException numberFormatException) {}
    } 
    return article;
  }
  
  private NewsgroupInfo[] __readNewsgroupListing() throws IOException {
    DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
    Vector<NewsgroupInfo> list = new Vector<>(2048);
    try {
      String line;
      while ((line = dotTerminatedMessageReader.readLine()) != null) {
        NewsgroupInfo tmp = __parseNewsgroupListEntry(line);
        if (tmp != null) {
          list.addElement(tmp);
          continue;
        } 
        throw new MalformedServerReplyException(line);
      } 
    } finally {
      dotTerminatedMessageReader.close();
    } 
    int size;
    if ((size = list.size()) < 1)
      return new NewsgroupInfo[0]; 
    NewsgroupInfo[] info = new NewsgroupInfo[size];
    list.copyInto((Object[])info);
    return info;
  }
  
  private BufferedReader __retrieve(int command, String articleId, ArticleInfo pointer) throws IOException {
    if (articleId != null) {
      if (!NNTPReply.isPositiveCompletion(sendCommand(command, articleId)))
        return null; 
    } else if (!NNTPReply.isPositiveCompletion(sendCommand(command))) {
      return null;
    } 
    if (pointer != null)
      __parseArticlePointer(getReplyString(), pointer); 
    return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
  }
  
  private BufferedReader __retrieve(int command, long articleNumber, ArticleInfo pointer) throws IOException {
    if (!NNTPReply.isPositiveCompletion(sendCommand(command, 
          Long.toString(articleNumber))))
      return null; 
    if (pointer != null)
      __parseArticlePointer(getReplyString(), pointer); 
    return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
  }
  
  public BufferedReader retrieveArticle(String articleId, ArticleInfo pointer) throws IOException {
    return __retrieve(0, articleId, pointer);
  }
  
  public Reader retrieveArticle(String articleId) throws IOException {
    return retrieveArticle(articleId, (ArticleInfo)null);
  }
  
  public Reader retrieveArticle() throws IOException {
    return retrieveArticle((String)null);
  }
  
  public BufferedReader retrieveArticle(long articleNumber, ArticleInfo pointer) throws IOException {
    return __retrieve(0, articleNumber, pointer);
  }
  
  public BufferedReader retrieveArticle(long articleNumber) throws IOException {
    return retrieveArticle(articleNumber, (ArticleInfo)null);
  }
  
  public BufferedReader retrieveArticleHeader(String articleId, ArticleInfo pointer) throws IOException {
    return __retrieve(3, articleId, pointer);
  }
  
  public Reader retrieveArticleHeader(String articleId) throws IOException {
    return retrieveArticleHeader(articleId, (ArticleInfo)null);
  }
  
  public Reader retrieveArticleHeader() throws IOException {
    return retrieveArticleHeader((String)null);
  }
  
  public BufferedReader retrieveArticleHeader(long articleNumber, ArticleInfo pointer) throws IOException {
    return __retrieve(3, articleNumber, pointer);
  }
  
  public BufferedReader retrieveArticleHeader(long articleNumber) throws IOException {
    return retrieveArticleHeader(articleNumber, (ArticleInfo)null);
  }
  
  public BufferedReader retrieveArticleBody(String articleId, ArticleInfo pointer) throws IOException {
    return __retrieve(1, articleId, pointer);
  }
  
  public Reader retrieveArticleBody(String articleId) throws IOException {
    return retrieveArticleBody(articleId, (ArticleInfo)null);
  }
  
  public Reader retrieveArticleBody() throws IOException {
    return retrieveArticleBody((String)null);
  }
  
  public BufferedReader retrieveArticleBody(long articleNumber, ArticleInfo pointer) throws IOException {
    return __retrieve(1, articleNumber, pointer);
  }
  
  public BufferedReader retrieveArticleBody(long articleNumber) throws IOException {
    return retrieveArticleBody(articleNumber, (ArticleInfo)null);
  }
  
  public boolean selectNewsgroup(String newsgroup, NewsgroupInfo info) throws IOException {
    if (!NNTPReply.isPositiveCompletion(group(newsgroup)))
      return false; 
    if (info != null)
      __parseGroupReply(getReplyString(), info); 
    return true;
  }
  
  public boolean selectNewsgroup(String newsgroup) throws IOException {
    return selectNewsgroup(newsgroup, (NewsgroupInfo)null);
  }
  
  public String listHelp() throws IOException {
    if (!NNTPReply.isInformational(help()))
      return null; 
    StringWriter help = new StringWriter();
    DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
    Util.copyReader((Reader)dotTerminatedMessageReader, help);
    dotTerminatedMessageReader.close();
    help.close();
    return help.toString();
  }
  
  public String[] listOverviewFmt() throws IOException {
    if (!NNTPReply.isPositiveCompletion(sendCommand("LIST", "OVERVIEW.FMT")))
      return null; 
    DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
    ArrayList<String> list = new ArrayList<>();
    String line;
    while ((line = dotTerminatedMessageReader.readLine()) != null)
      list.add(line); 
    dotTerminatedMessageReader.close();
    return list.<String>toArray(new String[list.size()]);
  }
  
  public boolean selectArticle(String articleId, ArticleInfo pointer) throws IOException {
    if (articleId != null) {
      if (!NNTPReply.isPositiveCompletion(stat(articleId)))
        return false; 
    } else if (!NNTPReply.isPositiveCompletion(stat())) {
      return false;
    } 
    if (pointer != null)
      __parseArticlePointer(getReplyString(), pointer); 
    return true;
  }
  
  public boolean selectArticle(String articleId) throws IOException {
    return selectArticle(articleId, (ArticleInfo)null);
  }
  
  public boolean selectArticle(ArticleInfo pointer) throws IOException {
    return selectArticle((String)null, pointer);
  }
  
  public boolean selectArticle(long articleNumber, ArticleInfo pointer) throws IOException {
    if (!NNTPReply.isPositiveCompletion(stat(articleNumber)))
      return false; 
    if (pointer != null)
      __parseArticlePointer(getReplyString(), pointer); 
    return true;
  }
  
  public boolean selectArticle(long articleNumber) throws IOException {
    return selectArticle(articleNumber, (ArticleInfo)null);
  }
  
  public boolean selectPreviousArticle(ArticleInfo pointer) throws IOException {
    if (!NNTPReply.isPositiveCompletion(last()))
      return false; 
    if (pointer != null)
      __parseArticlePointer(getReplyString(), pointer); 
    return true;
  }
  
  public boolean selectPreviousArticle() throws IOException {
    return selectPreviousArticle((ArticleInfo)null);
  }
  
  public boolean selectNextArticle(ArticleInfo pointer) throws IOException {
    if (!NNTPReply.isPositiveCompletion(next()))
      return false; 
    if (pointer != null)
      __parseArticlePointer(getReplyString(), pointer); 
    return true;
  }
  
  public boolean selectNextArticle() throws IOException {
    return selectNextArticle((ArticleInfo)null);
  }
  
  public NewsgroupInfo[] listNewsgroups() throws IOException {
    if (!NNTPReply.isPositiveCompletion(list()))
      return null; 
    return __readNewsgroupListing();
  }
  
  public Iterable<String> iterateNewsgroupListing() throws IOException {
    if (NNTPReply.isPositiveCompletion(list()))
      return new ReplyIterator(this._reader_); 
    throw new IOException("LIST command failed: " + getReplyString());
  }
  
  public Iterable<NewsgroupInfo> iterateNewsgroups() throws IOException {
    return new NewsgroupIterator(iterateNewsgroupListing());
  }
  
  public NewsgroupInfo[] listNewsgroups(String wildmat) throws IOException {
    if (!NNTPReply.isPositiveCompletion(listActive(wildmat)))
      return null; 
    return __readNewsgroupListing();
  }
  
  public Iterable<String> iterateNewsgroupListing(String wildmat) throws IOException {
    if (NNTPReply.isPositiveCompletion(listActive(wildmat)))
      return new ReplyIterator(this._reader_); 
    throw new IOException("LIST ACTIVE " + wildmat + " command failed: " + getReplyString());
  }
  
  public Iterable<NewsgroupInfo> iterateNewsgroups(String wildmat) throws IOException {
    return new NewsgroupIterator(iterateNewsgroupListing(wildmat));
  }
  
  public NewsgroupInfo[] listNewNewsgroups(NewGroupsOrNewsQuery query) throws IOException {
    if (!NNTPReply.isPositiveCompletion(newgroups(
          query.getDate(), query.getTime(), 
          query.isGMT(), query.getDistributions())))
      return null; 
    return __readNewsgroupListing();
  }
  
  public Iterable<String> iterateNewNewsgroupListing(NewGroupsOrNewsQuery query) throws IOException {
    if (NNTPReply.isPositiveCompletion(newgroups(
          query.getDate(), query.getTime(), 
          query.isGMT(), query.getDistributions())))
      return new ReplyIterator(this._reader_); 
    throw new IOException("NEWGROUPS command failed: " + getReplyString());
  }
  
  public Iterable<NewsgroupInfo> iterateNewNewsgroups(NewGroupsOrNewsQuery query) throws IOException {
    return new NewsgroupIterator(iterateNewNewsgroupListing(query));
  }
  
  public String[] listNewNews(NewGroupsOrNewsQuery query) throws IOException {
    if (!NNTPReply.isPositiveCompletion(
        newnews(query.getNewsgroups(), query.getDate(), query.getTime(), 
          query.isGMT(), query.getDistributions())))
      return null; 
    Vector<String> list = new Vector<>();
    DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
    try {
      String line;
      while ((line = dotTerminatedMessageReader.readLine()) != null)
        list.addElement(line); 
    } finally {
      dotTerminatedMessageReader.close();
    } 
    int size = list.size();
    if (size < 1)
      return new String[0]; 
    String[] result = new String[size];
    list.copyInto((Object[])result);
    return result;
  }
  
  public Iterable<String> iterateNewNews(NewGroupsOrNewsQuery query) throws IOException {
    if (NNTPReply.isPositiveCompletion(newnews(
          query.getNewsgroups(), query.getDate(), query.getTime(), 
          query.isGMT(), query.getDistributions())))
      return new ReplyIterator(this._reader_); 
    throw new IOException("NEWNEWS command failed: " + getReplyString());
  }
  
  public boolean completePendingCommand() throws IOException {
    return NNTPReply.isPositiveCompletion(getReply());
  }
  
  public Writer postArticle() throws IOException {
    if (!NNTPReply.isPositiveIntermediate(post()))
      return null; 
    return (Writer)new DotTerminatedMessageWriter(this._writer_);
  }
  
  public Writer forwardArticle(String articleId) throws IOException {
    if (!NNTPReply.isPositiveIntermediate(ihave(articleId)))
      return null; 
    return (Writer)new DotTerminatedMessageWriter(this._writer_);
  }
  
  public boolean logout() throws IOException {
    return NNTPReply.isPositiveCompletion(quit());
  }
  
  public boolean authenticate(String username, String password) throws IOException {
    int replyCode = authinfoUser(username);
    if (replyCode == 381) {
      replyCode = authinfoPass(password);
      if (replyCode == 281) {
        this._isAllowedToPost = true;
        return true;
      } 
    } 
    return false;
  }
  
  private BufferedReader __retrieveArticleInfo(String articleRange) throws IOException {
    if (!NNTPReply.isPositiveCompletion(xover(articleRange)))
      return null; 
    return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
  }
  
  public BufferedReader retrieveArticleInfo(long articleNumber) throws IOException {
    return __retrieveArticleInfo(Long.toString(articleNumber));
  }
  
  public BufferedReader retrieveArticleInfo(long lowArticleNumber, long highArticleNumber) throws IOException {
    return 
      __retrieveArticleInfo(String.valueOf(lowArticleNumber) + "-" + 
        highArticleNumber);
  }
  
  public Iterable<Article> iterateArticleInfo(long lowArticleNumber, long highArticleNumber) throws IOException {
    BufferedReader info = retrieveArticleInfo(lowArticleNumber, highArticleNumber);
    if (info == null)
      throw new IOException("XOVER command failed: " + getReplyString()); 
    return new ArticleIterator(new ReplyIterator(info, false));
  }
  
  private BufferedReader __retrieveHeader(String header, String articleRange) throws IOException {
    if (!NNTPReply.isPositiveCompletion(xhdr(header, articleRange)))
      return null; 
    return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
  }
  
  public BufferedReader retrieveHeader(String header, long articleNumber) throws IOException {
    return __retrieveHeader(header, Long.toString(articleNumber));
  }
  
  public BufferedReader retrieveHeader(String header, long lowArticleNumber, long highArticleNumber) throws IOException {
    return 
      __retrieveHeader(header, String.valueOf(lowArticleNumber) + "-" + highArticleNumber);
  }
  
  @Deprecated
  public Reader retrieveHeader(String header, int lowArticleNumber, int highArticleNumber) throws IOException {
    return retrieveHeader(header, lowArticleNumber, highArticleNumber);
  }
  
  @Deprecated
  public Reader retrieveArticleInfo(int lowArticleNumber, int highArticleNumber) throws IOException {
    return retrieveArticleInfo(lowArticleNumber, highArticleNumber);
  }
  
  @Deprecated
  public Reader retrieveHeader(String a, int b) throws IOException {
    return retrieveHeader(a, b);
  }
  
  @Deprecated
  public boolean selectArticle(int a, ArticlePointer ap) throws IOException {
    ArticleInfo ai = __ap2ai(ap);
    boolean b = selectArticle(a, ai);
    __ai2ap(ai, ap);
    return b;
  }
  
  @Deprecated
  public Reader retrieveArticleInfo(int lowArticleNumber) throws IOException {
    return retrieveArticleInfo(lowArticleNumber);
  }
  
  @Deprecated
  public boolean selectArticle(int a) throws IOException {
    return selectArticle(a);
  }
  
  @Deprecated
  public Reader retrieveArticleHeader(int a) throws IOException {
    return retrieveArticleHeader(a);
  }
  
  @Deprecated
  public Reader retrieveArticleHeader(int a, ArticlePointer ap) throws IOException {
    ArticleInfo ai = __ap2ai(ap);
    Reader rdr = retrieveArticleHeader(a, ai);
    __ai2ap(ai, ap);
    return rdr;
  }
  
  @Deprecated
  public Reader retrieveArticleBody(int a) throws IOException {
    return retrieveArticleBody(a);
  }
  
  @Deprecated
  public Reader retrieveArticle(int articleNumber, ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    Reader rdr = retrieveArticle(articleNumber, ai);
    __ai2ap(ai, pointer);
    return rdr;
  }
  
  @Deprecated
  public Reader retrieveArticle(int articleNumber) throws IOException {
    return retrieveArticle(articleNumber);
  }
  
  @Deprecated
  public Reader retrieveArticleBody(int a, ArticlePointer ap) throws IOException {
    ArticleInfo ai = __ap2ai(ap);
    Reader rdr = retrieveArticleBody(a, ai);
    __ai2ap(ai, ap);
    return rdr;
  }
  
  @Deprecated
  public Reader retrieveArticle(String articleId, ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    Reader rdr = retrieveArticle(articleId, ai);
    __ai2ap(ai, pointer);
    return rdr;
  }
  
  @Deprecated
  public Reader retrieveArticleBody(String articleId, ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    Reader rdr = retrieveArticleBody(articleId, ai);
    __ai2ap(ai, pointer);
    return rdr;
  }
  
  @Deprecated
  public Reader retrieveArticleHeader(String articleId, ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    Reader rdr = retrieveArticleHeader(articleId, ai);
    __ai2ap(ai, pointer);
    return rdr;
  }
  
  @Deprecated
  public boolean selectArticle(String articleId, ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    boolean b = selectArticle(articleId, ai);
    __ai2ap(ai, pointer);
    return b;
  }
  
  @Deprecated
  public boolean selectArticle(ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    boolean b = selectArticle(ai);
    __ai2ap(ai, pointer);
    return b;
  }
  
  @Deprecated
  public boolean selectNextArticle(ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    boolean b = selectNextArticle(ai);
    __ai2ap(ai, pointer);
    return b;
  }
  
  @Deprecated
  public boolean selectPreviousArticle(ArticlePointer pointer) throws IOException {
    ArticleInfo ai = __ap2ai(pointer);
    boolean b = selectPreviousArticle(ai);
    __ai2ap(ai, pointer);
    return b;
  }
  
  private ArticleInfo __ap2ai(ArticlePointer ap) {
    if (ap == null)
      return null; 
    ArticleInfo ai = new ArticleInfo();
    return ai;
  }
  
  private void __ai2ap(ArticleInfo ai, ArticlePointer ap) {
    if (ap != null) {
      ap.articleId = ai.articleId;
      ap.articleNumber = (int)ai.articleNumber;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\NNTPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */