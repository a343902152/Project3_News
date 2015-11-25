package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public  class NewsController {
	@RequestMapping(value="/findNewsCategories.do")
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
		return categories;
	}
	@RequestMapping(value="/findNewsByCategory.do")
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
					System.out.println(news.getId());
				}
			}else{
				System.out.println("catexxx");
				Document doc = Jsoup.connect("http://news.cqu.edu.cn/news/article/list.php?catid="+categoryId).get();
				Elements liphoto = doc.select("div.liphoto div.row1");
				for(Element row:liphoto)
				{
					Element link=row.select("a").first();
					Element img=row.select("img").first();
					News news=new News(link.attr("href"),img.attr("alt"),"","http://news.cqu.edu.cn/"+img.attr("src"));
					newsList.add(news);
					System.out.println(news.getId());
				}
			}
		} catch (IOException e) {
			System.out.println("error");
			e.printStackTrace();
		}
		System.out.println(newsList.size());
		return newsList;
	}

	//查找所有新闻（id&title）
	public static List<News> findAllNews() throws IOException {
		List<String> cat=new ArrayList<String>();
		List<NewsCategory> category= findNewsCategories();
		List<News> newsList=new ArrayList<News>();
		for(int i=0;i<category.size();i++)
		{
			cat.add(category.get(i).getId());
			if(cat.get(i).equals("00"))
			{
				List<News> newsList1=new ArrayList<News>();
				Document doc = Jsoup.connect("http://news.cqu.edu.cn/news/").get();
				Elements topnews = doc.select("div.topnews li.tag_title");
				for(Element row:topnews)
				{
					Element link=row.select("a").first();
					News news=new News(link.attr("href"),link.attr("title"),"","");
					newsList1.add(news);
				}
				newsList.addAll(newsList1);
			}
			else{
				List<News> newsList2=new ArrayList<News>();
				Document doc = Jsoup.connect("http://news.cqu.edu.cn/news/article/list.php?catid="+cat.get(i)).get();
				Elements liphoto = doc.select("div.liphoto div.row1");
				for(Element row:liphoto)
				{
					Element link=row.select("a").first();
					Element img=row.select("img").first();
					News news=new News(link.attr("href"),img.attr("alt"),"","http://news.cqu.edu.cn/"+img.attr("src"));
					newsList2.add(news);
				}
				newsList.addAll(newsList2);
			}
		}
		return newsList;
	}
	
	@RequestMapping(value="/findNews.do")
	public static News findNewsById(String newsId)
	{
		News news=new News();
		try {
			Document doc = Jsoup.connect("http://news.cqu.edu.cn"+newsId).get();
			news.setTitle(doc.select("div.title h1").first().text());
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
//	public static void  main(String args[])
//	{
//		NewsController c=new NewsController();
//		News news=c.findNewsById("/news/article/show.php?itemid=64700");
//		System.out.println(news.getContent());
//	}
}
