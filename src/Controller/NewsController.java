package Controller;

import Model.MyMessage;
import Model.WebCrawler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import util.IndexManager;
import Model.News;
import util.FileOperation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by hp on 2015/11/24.
 */
@WebServlet(name = "NewsController", urlPatterns = {"/controller"})
public class NewsController extends HttpServlet {

    /**
     * 存放一页一页的新闻
     */
    private Vector<List<News>> newsVector=new Vector<List<News>>();
    private IndexManager indexManager=new IndexManager();


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=GBK");
        String action = request.getParameter("action");
        System.out.println(action);
        if (action.equals("getNewsList")) {
            // 获取新闻列表
            String type=request.getParameter("newsType");
            System.out.println(type);
            getNewsList(request, response, type);
        }else if(action.equals("getNewsById")){
            // 获取新闻内容
            String newsid=request.getParameter("newsId");
            System.out.println(newsid);
            getNewsById(request,response,newsid);
        }else if(action.equals("serachNews")){
            String key=request.getParameter("key");
            System.out.println(key);
            searchNewsByKey(request,response,key);
        }else if(action.equals("getNewsByPage")){
            String pagenum=request.getParameter("pagenum");
            System.out.println("pagenum="+pagenum);
            getNewsByPage(response,Integer.valueOf(pagenum));
        }
    }

    /**
     * 根据key查找新闻
     * 返回json格式的news
     * @param request
     * @param response
     * @param key
     */
    private void searchNewsByKey(HttpServletRequest request, HttpServletResponse response,
                                 String key){
        try{
            PrintWriter out=response.getWriter();
            List<News> list=indexManager.searchIndex(key);
            Gson gson=new Gson();
            String res= gson.toJson(list);

            out.println(res);
            out.flush();
            out.close();	//关闭输出流对象
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 根据ID获取新闻内容、作者、时间等
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getNewsById(HttpServletRequest request, HttpServletResponse response,String newsid){
        try{
            PrintWriter out=response.getWriter();
            System.out.println("Getnews");

            List<News> list=new ArrayList<News>();
            News news= WebCrawler.findNewsById(newsid);
            list.add(news);
            Gson gson =new Gson();
            String res=gson.toJson(list);

            System.out.println("news detail"+res);
            out.print(res);
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 吧新闻list转化成json传递到前端
     * @param request
     * @param response
     * @param type
     */
    public void getNewsList(HttpServletRequest request, HttpServletResponse response,String type) {
        try {
            Gson gson=new Gson();
            PrintWriter out = response.getWriter();

            List<News> newsList;
            String newsListJson= FileOperation.readLog(type + ".txt");

            // 查找新闻
            if(newsListJson!=null){
                // 如果本地已经有数据,就直接读取出来
                newsList = gson.fromJson(newsListJson, new TypeToken<List<News>>() {}.getType());
            }else{
                newsList = WebCrawler.findNewsByCategory(type);
                // 写入文件
                FileOperation.writeLog(type + ".txt", gson.toJson(newsList));
            }
            // 建立索引
            indexManager.buildIndexByType(newsList, type);

            // 分页显示,返回的是有多少页
            int pages=doPaging(newsList);

            String res=gson.toJson(newsVector.get(0));
            // 页数跟首页的内容封装在MyMessage中，再转乘json传到前台
            MyMessage myMessage=new MyMessage(pages,res);

            // 添加到list中，解析成json返回给前端
            List<MyMessage> tempList=new ArrayList<MyMessage>();
            tempList.add(myMessage);
            out.println(gson.toJson(tempList));

            out.flush();
            out.close();    //关闭输出流对象
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 吧新闻分页的函数
     * 一页只显示perPageCount条新闻
     */
    public int doPaging(List<News> list){
        // 下面为分页
        int perPageCount=30;
        int pages=(list.size()+perPageCount-1)/perPageCount;
        if(newsVector==null)
            newsVector=new Vector<List<News>>();
        newsVector.clear();
        for(int i=0;i<pages-1;i++)
        {
            newsVector.add(
                    list.subList(perPageCount*i,perPageCount*(i+1)-1));
        }
        newsVector.add(
                list.subList(perPageCount*(pages-1),list.size()-1));
        return pages;
    }

    /**
     * 获取第pagenum页的新闻 直接println到前台
     * @param response
     * @param pagenum
     */
    public void getNewsByPage( HttpServletResponse response,int pagenum){
        try{
            PrintWriter out=response.getWriter();

            Gson gson=new Gson();
            String res=gson.toJson(newsVector.get(pagenum));
            System.out.println("get page,pagenum="+pagenum+
                    "res="+res);
            out.println(res);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
