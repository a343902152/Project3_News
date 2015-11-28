package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网络爬虫类，用于获取数据
 */
@RestController
public  class WebCrawler {
	public static List<NewsCategory> findNewsCategories()
	{
		List<NewsCategory> categories=new ArrayList<NewsCategory>();
		categories.add(new NewsCategory("00","重要新闻"));
		categories.add(new NewsCategory("46","重大科研"));
		categories.add(new NewsCategory("47","重大学术" ));
		categories.add(new NewsCategory("48","重大教学"));
		categories.add(new NewsCategory("53","招生就业"));
		categories.add(new NewsCategory("54","交流合作"));
		categories.add(new NewsCategory("71","校董校企"));
		categories.add(new NewsCategory("6","媒体重大"));
		categories.add(new NewsCategory("49","重大人物"));
		categories.add(new NewsCategory("51","获奖成果"));
		categories.add(new NewsCategory("63","杰出校友"));
		categories.add(new NewsCategory("41","文体赛事"));
		categories.add(new NewsCategory("9","社团活动"));
		categories.add(new NewsCategory("50","重大风采"));

		return categories;
	}
	public static List<News> findNewsByCategory(String categoryId)
	{

		List<News> newsList=new ArrayList<News>();
		try {
			//当为重要新闻时，没有固定的归类，所以要分情况判断
			//categoryId=00表示重要新闻
			if(categoryId.equals("00")){
				Document doc = Jsoup.connect("http://news.cqu.edu.cn/news/").get();
				Elements topnews = doc.select("div.topnews li.tag_title");
				for(Element row:topnews)
				{
					Element link=row.select("a").first();
					News news=new News(link.attr("href"),link.attr("title"),"","");
					newsList.add(news);
				}
			}else{
				Document doc = Jsoup.connect("http://news.cqu.edu.cn/news/article/list.php?catid="+categoryId).get();
				/**获取页码*/
				Elements page=doc.select("div.pages");
				Element p=page.select("b").last();
				String pageNum=p.text().substring(p.text().indexOf("/")+1);
				System.out.println("pageNumber:"+pageNum);
//				//有缩略图,只在第一页中添加到列表，其他页码下不再重复添加
//				Elements liphoto = doc.select("div.liphoto div.row1");
//				for(Element row:liphoto)
//				{
//					Element link=row.select("a").first();
//					Element img=row.select("img").first();
//					News news=new News(link.attr("href"),img.attr("alt"),"",
//							"http://news.cqu.edu.cn/"+img.attr("src"));
//					newsList.add(news);
//				}

				//将每一页中新闻添加到newslist
				for(int i=1;i<=Integer.parseInt(pageNum);i++)
				{
//					System.out.println("页码："+i);
					//无缩略图
					Document doc1 = Jsoup.connect("http://news.cqu.edu.cn/news/article/" +
							"list.php?catid="+categoryId+"&page="+i).get();
					Elements linews = doc1.select("div.linews li.tag_title");
					for(Element row:linews)
					{
						Element link=row.select("a").first();
						News news=new News(link.attr("href"),link.attr("title"),"","");
						newsList.add(news);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newsList;
	}

	//抓取所有新闻（返回newsList 包含新闻id&title）
	public static List<News> findAllNews() throws IOException {
		List<String> cat=new ArrayList<String>();
		List<NewsCategory> category= findNewsCategories();
		List<News> newsList=new ArrayList<News>();
		for(int i=0;i<category.size();i++)
		{
			cat.add(category.get(i).getId());
			newsList.addAll(findNewsByCategory(category.get(i).getId()));

		}
		return newsList;
	}


	public static News findNewsById(String newsId)
	{
		News news=new News();
		try {
			Document doc = Jsoup.connect("http://news.cqu.edu.cn"+newsId).get();
			news.setTitle(doc.select("div.title h1").first().text());
			news.setDate(doc.select("div.clear.author span.datetime").text());
			String author=doc.select("div.clear.author").text();
			author=author.substring(25, 30).replaceAll(" ","");
			news.setAuthor(author.substring(0,author.length()-2));
			Element content=doc.select("#zoom").first();
			Elements imgs=doc.select("img");
			for(Element img:imgs)
			{
				String src=img.attr("src");
				img.attributes().remove("height");
				img.attributes().remove("alt");
				img.attr("width","100%");
				img.attr("src","http://news.cqu.edu.cn"+src );
			}
			content.attributes().remove("id");
			content.attributes().remove("class");
			news.setContent(content.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return news;
	}

//	public static void main(String[] args){
//		WebCrawler c=new WebCrawler();
//		News news=c.findNewsById("/news/article/2015/0820/article_69162.html");
//		System.out.println(news.getTitle()+news.getAuthor());
//	}
}
