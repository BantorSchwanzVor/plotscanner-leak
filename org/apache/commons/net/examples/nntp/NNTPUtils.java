package org.apache.commons.net.examples.nntp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.nntp.Article;
import org.apache.commons.net.nntp.NNTPClient;

public class NNTPUtils {
  public static List<Article> getArticleInfo(NNTPClient client, long lowArticleNumber, long highArticleNumber) throws IOException {
    List<Article> articles = new ArrayList<>();
    Iterable<Article> arts = client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
    for (Article article : arts)
      articles.add(article); 
    return articles;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\nntp\NNTPUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */