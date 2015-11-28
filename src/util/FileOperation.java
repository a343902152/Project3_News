package util;

import java.io.*;

/**
 * Created by hp on 2015/11/28.
 * 读取和输入文本
 */
public class FileOperation {

    public static void writeLog(String path,String str)
    {
        try
        {
            System.out.println("write path="+"newsData/"+path);
            File file=new File("D:\\JAVA_project\\Project3_News\\newsData\\"+path);
            if(!file.exists())
                file.createNewFile();
            FileOutputStream out=new FileOutputStream(file,false); //如果追加方式用true
            StringBuffer sb=new StringBuffer();
            sb.append(str+"\n");
            out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
            out.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }
    public static String readLog(String path)
    {
        StringBuffer sb=new StringBuffer();
        String tempstr=null;
        try
        {
            File file=new File("D:\\JAVA_project\\Project3_News\\newsData\\"+path);
            System.out.println("read path="+"D:\\JAVA_project\\Project3_News\\newsData\\"+path);
            if(!file.exists())
                return null;
            //另一种读取方式
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis,"utf-8"));
            while((tempstr=br.readLine())!=null)
                sb.append(tempstr);
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
        return sb.toString();
    }

}
