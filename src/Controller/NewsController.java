package Controller;

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
            // ��ȡ�����б�
            String type=request.getParameter("newsType");
            System.out.println(type);
            getNewsList(request,response,type);
        }else if(action.equals("getNewsById")){
            // ��ȡ��������
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
     * ��������
     * @param request
     * @param response
     * @param key
     */
    private void searchNewsByKey(HttpServletRequest request, HttpServletResponse response,
                                 String key){
        try{
            PrintWriter out=response.getWriter();

            List<News> list=indexManager.searchIndex(key);
            String res=listToHTML(list);

            out.println(res);
            out.flush();
            out.close();	//�ر����������
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * ��ȡ�����б�
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getNewsList(HttpServletRequest request, HttpServletResponse response,
                             String type) {
        try{
            PrintWriter out=response.getWriter();

            // ��������
            List<News> list= domain.NewsController.findNewsByCategory(type);
            // ��������
            indexManager.buildIndexByType(list,type);

            String res = listToHTML(list);

            out.println(res);
            out.flush();
            out.close();	//�ر����������
        }catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // �ɻ�ȡ��������listת����html
    private String listToHTML(List<News> list){
        StringBuilder str=new StringBuilder("<ul>");
        for(int i=0;i<list.size();i++){
            str.append("<li>");
            str.append("<a id=\""+list.get(i).getId()+"\" " +"class=\"list-group-item\""+
                    "href=\"newsDetail.html\">");
            str.append(list.get(i).getTitle());
            str.append("</a>");
            str.append("</li>");
        }
        str.append("</ul>");
        return str.toString();
    }


    /**
     * ����ID��ȡ��������+ͼƬ
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
    // ��ȡ��������ת����HTML
    private String newsToHTML(News news){
        StringBuilder str=new StringBuilder("<h1>");
        str.append(news.getContent());
//        str.append("news news news news news news news news news news news ");
        str.append("</h1>");
        return str.toString();
    }

    @RequestMapping(value="/test.do")
    public List<News> test(String newsType)
    {
        System.out.println("testDo");
        List<News> list= domain.NewsController.findNewsByCategory(newsType);
        return list;
    }
}
