package domain;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/11/22.
 * IndexManager?
 */
public class IndexUtil {

    /**
     * 建立索引
     * @param writer
     * @throws Exception
     */
    public void buildIndex(IndexWriter writer, List<News> list)
            throws Exception {
        writer.deleteAll();//清空索引库里已存在的文档（document）
        System.out.println("buildIndex()->新闻条数 :"+list.size());

        for(News news :list){
            Document doc = new Document();//创建索引库的文档  
            doc.add(new Field("id",news.getId(), Field.Store.YES, Field.Index.NO));
            doc.add(new Field("title",news.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
//            doc.add(new Field("content",news.getContent(), Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);//将文档写入索引库  
        }
        int count =writer.numDocs();
        writer.forceMerge(1000);//合并索引库文件  
        writer.close();
        System.out.println("buildIndex()->存入索引库的数量：" + count);
    }

    /**
     * 从索引库中搜索你要查询的数据 
     * @param searcher
     * @throws IOException
     */
    public List<News> searchIndex(IndexSearcher searcher, String key){
        try {
            List<News> newslist = new ArrayList<News>();
            QueryParser parser = new QueryParser(Version.LUCENE_36, "title",
                    new IKAnalyzer());
            parser.setDefaultOperator(QueryParser.Operator.AND);
            Query query = parser.parse(key);
            long begin = (long) System.currentTimeMillis();
            TopDocs docs = searcher.search(query, 100);//查找
            System.out.println("searcherDoc()->数目：" + docs.totalHits);
            for (ScoreDoc doc : docs.scoreDocs) {//获取查找的文档的属性数据
                int docID = doc.doc;
                Document document = searcher.doc(docID);
                String id = document.get("id");
                String title = document.get("title");
                News news = new News(id, title, "", "");
                newslist.add(news);
            }
            long end = (long) System.currentTimeMillis();
            long time = end - begin;
            System.out.println("查询时间 :" + time + "ms");
            return newslist;
        }catch (ParseException e1){
            e1.printStackTrace();
        }catch (IOException e2){
            e2.printStackTrace();
        }
        // 出错就返回null
        return null;
    }
}