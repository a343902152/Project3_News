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
     * ��������
     * @param writer
     * @throws Exception
     */
    public void buildIndex(IndexWriter writer, List<News> list)
            throws Exception {
        writer.deleteAll();//������������Ѵ��ڵ��ĵ���document��
        System.out.println("buildIndex()->�������� :"+list.size());

        for(News news :list){
            Document doc = new Document();//������������ĵ�  
            doc.add(new Field("id",news.getId(), Field.Store.YES, Field.Index.NO));
            doc.add(new Field("title",news.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
//            doc.add(new Field("content",news.getContent(), Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);//���ĵ�д��������  
        }
        int count =writer.numDocs();
        writer.forceMerge(1000);//�ϲ��������ļ�  
        writer.close();
        System.out.println("buildIndex()->�����������������" + count);
    }

    /**
     * ����������������Ҫ��ѯ������ 
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
            TopDocs docs = searcher.search(query, 100);//����
            System.out.println("searcherDoc()->��Ŀ��" + docs.totalHits);
            for (ScoreDoc doc : docs.scoreDocs) {//��ȡ���ҵ��ĵ�����������
                int docID = doc.doc;
                Document document = searcher.doc(docID);
                String id = document.get("id");
                String title = document.get("title");
                News news = new News(id, title, "", "");
                newslist.add(news);
            }
            long end = (long) System.currentTimeMillis();
            long time = end - begin;
            System.out.println("��ѯʱ�� :" + time + "ms");
            return newslist;
        }catch (ParseException e1){
            e1.printStackTrace();
        }catch (IOException e2){
            e2.printStackTrace();
        }
        // ����ͷ���null
        return null;
    }
}