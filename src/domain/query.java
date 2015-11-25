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
import java.util.List;

/**
 * Created by lenovo on 2015/11/22.
 */
public class query {
    /**
     * ��������������Դд�������� 
     * @param writer
     * @throws Exception
     */
    public void buildDocs(IndexWriter writer,List<News> list)throws Exception {
        writer.deleteAll();//������������Ѵ��ڵ��ĵ���document��  
//        List<News> list = NewsController.findNewsByCategory("00");//�õ�������Դ  
//        List<News> list=NewsController.findAllNews();
        System.out.println("buildDocs()->�������� :"+list.size());
        
        for(News news :list){
            Document doc = new Document();//������������ĵ�  
            doc.add(new Field("id",news.getId(), Field.Store.YES, Field.Index.NO));
            doc.add(new Field("title",news.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("content",news.getContent(), Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);//���ĵ�д��������  
        }
        int count =writer.numDocs();
        writer.forceMerge(1000);//�ϲ��������ļ�  
        writer.close();
        System.out.println("buildDocs()->�����������������" + count);
    }

    /**
     * ����������������Ҫ��ѯ������ 
     * @param searcher
     * @throws IOException
     */
    public void searcherDocs(IndexSearcher searcher,String key) throws IOException, ParseException {
        
//        String queryString = "*����*";
//        Term term = new Term("title",queryString);
//        WildcardQuery query = new WildcardQuery(term);
        QueryParser parser = new QueryParser(Version.LUCENE_36, "title",
                new IKAnalyzer());
        parser.setDefaultOperator(QueryParser.Operator.AND);
        Query query = parser.parse(key);
//        Term term =new Term("title", "³�ཱ");//��ѯ��������˼����Ҫ�����Ա�Ϊ��man������  
//        TermQuery query =new TermQuery(term);
        long begin=(long)System.currentTimeMillis();
        TopDocs docs =searcher.search(query, 100);//����  
        System.out.println("searcherDoc()->��Ŀ��"+docs.totalHits);
        for(ScoreDoc doc:docs.scoreDocs){//��ȡ���ҵ��ĵ�����������  
            int docID=doc.doc;
            Document document =searcher.doc(docID);
            String str="ID:"+document.get("id")+",���⣺"+document.get("title");
            System.out.println("������Ϣ:"+str);
            
        }
        long end= (long) System.currentTimeMillis();
        long time=end-begin;
        System.out.println("��ѯʱ�� :"+time+"ms");
    }
}
