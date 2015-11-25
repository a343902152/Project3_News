package domain;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.List;
import java.util.Scanner;


/**
 * Created by lenovo on 2015/11/22.
 */
public class Testquery {
    private IndexWriter writer=null;
    private Directory directory=null;
    private IndexReader reader = null;
    private IndexSearcher searcher=null;
    private query demo =new query();

    /**初始化*/
    public void setUp() throws Exception {
        directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,new IKAnalyzer());
        writer = new IndexWriter(directory,config);
    }

    
    public void testAddDocAll()throws Exception {
        /**生成索引库*/
        System.out.println("建立索引");
        List<News> list=NewsController.findAllNews();
        demo.buildDocs(writer,list);

        System.out.println("查询数据");
        System.out.println("输入关键字");
        Scanner sc=new Scanner(System.in);
        String key=sc.nextLine();
        /**查询数据*/
        reader = IndexReader.open(directory);
        searcher =new IndexSearcher(reader);
        demo.searcherDocs(searcher,key);
    }
    public void testAddDocByCat(String cat)throws Exception {
        /**生成索引库*/
        System.out.println("建立索引");
        List<News> list=NewsController.findNewsByCategory(cat);
        demo.buildDocs(writer,list);

        System.out.println("查询数据");
        System.out.println("输入关键字");
        Scanner sc=new Scanner(System.in);
        String key=sc.nextLine();
        /**查询数据*/
        reader = IndexReader.open(directory);
        searcher =new IndexSearcher(reader);
        demo.searcherDocs(searcher,key);
    }
    
    public static void main(String[] args) throws Exception {
        Testquery t=new Testquery();
        t.setUp();
//        t.testAddDocByCat("00");
        t.testAddDocAll();
    }
}
