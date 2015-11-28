package Model;


/**
 * Created by lenovo on 2015/11/20.
 * 新闻，包括id 标题，内容，作者等
 */
public class News {
    private String id;
    private String title;
    private String content;
    private String author;
    private String date;
    private String img;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String thumb) {
        this.img = thumb;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public News() {
        super();
    }
    public News(String id, String title, String content, String img) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.img = img;
    }
}
