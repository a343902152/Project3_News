package Model;

/**
 * Created by hp on 2015/11/28.
 * ����ǰ��̨����json�����õ�
 */
public class MyMessage {
    private int pages;
    private String msg;

    public MyMessage(int pages,String msg){
        this.pages=pages;
        this.msg=msg;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
