package Controller;

import com.google.gson.Gson;
import domain.IndexManager;
import domain.News;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2015/11/24.
 */
@WebServlet(name = "NewsController", urlPatterns = {"/controller"})
public class NewsController extends HttpServlet {

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
        }
    }

    /**
     * 查找新闻
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
     * 根据ID获取新闻内容+图片
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getNewsById(HttpServletRequest request, HttpServletResponse response,String newsid){
        try{
            PrintWriter out=response.getWriter();
            System.out.println("Getnews");
            News news=domain.NewsController.findNewsById(newsid);
            String res=newsToHTML(news);
            out.print(res);
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    // 获取到的新闻转换到HTML
    private String newsToHTML(News news){
        StringBuilder str=new StringBuilder("<h1>");
        str.append(news.getContent());
//        str.append("news news news news news news news news news news news ");
        str.append("</h1>");
        return str.toString();
    }

    /**
     * 吧新闻list转化成json传递到前端
     * @param request
     * @param response
     * @param type
     */
    public void getNewsList(HttpServletRequest request, HttpServletResponse response,String type) {
        try {
            PrintWriter out = response.getWriter();

            // 查找新闻
            List<News> list = domain.NewsController.findNewsByCategory(type);
            // 建立索引
            indexManager.buildIndexByType(list, type);
            Gson gson=new Gson();
            String res=gson.toJson(list);
            System.out.println(res);

            out.println(res);
            out.flush();
            out.close();    //关闭输出流对象
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
