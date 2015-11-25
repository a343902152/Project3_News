package Controller;

import sun.plugin2.message.GetAppletMessage;

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


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=GBK");
        String action = request.getParameter("action");

        if (action.equals("getNewsList")) {
            // ��ȡ�����б�
            String type=request.getParameter("newsType");
            System.out.println(type);
            getNewsList(request,response,type);
        }else if(action.equals("getNewsById")){
            // ��ȡ��������
            getNewsById(request,response);
        }
    }


    // �ɻ�ȡ��������ת����html
    private String listToHTML(List<String> list){
        StringBuilder str=new StringBuilder("<ul>");
        for(int i=0;i<list.size();i++){
            str.append("<li>");
            str.append("<a id=\""+i+"\" " +"class=\"list-group-item\""+
                    "href=\"newsDetail.jsp?id=abc\">");
            str.append(list.get(i));
            str.append("</a>");
            str.append("</li>");
        }
        str.append("</ul>");
        return str.toString();
    }



    /**
     * ��ȡ�����б�
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getNewsList(HttpServletRequest request, HttpServletResponse response,
                             String type)
            throws ServletException, IOException {
        List<String> list=new ArrayList<String>();
        PrintWriter out=response.getWriter();
        if(type.equals("0")){
            list.add("a");
            list.add("bb");
            list.add("ccc");
        }else{
            list.add("zzzzzzz");
            list.add("ccccccccccccccccc");
            list.add("bbbbbbbbbbbbb");
        }
        String res = listToHTML(list);
        out.println(res);
        out.close();	//�ر����������
    }


    /**
     * ����ID��ȡ��������+ͼƬ
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getNewsById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        PrintWriter out=response.getWriter();
        System.out.println("Getnews");
        String res=newsToHTML();
        out.print(res);
        out.flush();
        out.close();
    }
    // ��ȡ��������ת����HTML
    private String newsToHTML(){
        StringBuilder str=new StringBuilder("<h1>");
        str.append("news news news news news news news news news news news ");
        str.append("</h1>");
        return str.toString();
    }
}
