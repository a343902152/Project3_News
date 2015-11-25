package domain;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.List;


/**
 * Created by lenovo on 2015/11/22.
 * Manager���ṩ���������Ͳ�ѯkey�Ĺ���
 */
public class IndexManager {
    private IndexWriter writer=null;
    private Directory directory=new RAMDirectory();
    private IndexReader reader = null;
    private IndexSearcher searcher=null;
    private IndexUtil indexUtil =new IndexUtil();

    public IndexManager(){
    }

    //�����������������
    public void buildIndexByType(List<News> list,String type){
        try {
            // ����directory��writer
            directory = new RAMDirectory();
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,new IKAnalyzer());
            writer = new IndexWriter(directory,config);
            /**����������*/
            indexUtil.buildIndex(writer, list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**��ѯ*/
    public List<News> searchIndex(String key){
        try {
            reader = IndexReader.open(directory);
            searcher = new IndexSearcher(reader);
            List<News> list = indexUtil.searchIndex(searcher, key);
            return list;
        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }
}