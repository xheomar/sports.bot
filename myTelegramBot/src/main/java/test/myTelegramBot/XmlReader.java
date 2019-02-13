package test.myTelegramBot;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import test.myTelegramBot.xml.XmlItem;

public class XmlReader 
{
	private static final String BASE_URL = "https://www.sports.ru/tribuna/blogs/";
	private static final String RSS_XML = "/rss.xml";
	
	public static void main(String[] args) throws IOException
	{
		getRssFeedLastItemId("zapiskipresledovatelya");
	}
	
	public static XmlItem getRssFeedLastItemId(String name) throws IOException 
	{
		Document doc;
		String title, description, id="", link="", pubDate, channelName;
		
		doc = Jsoup.connect(BASE_URL + name + RSS_XML).get();
				
		Pattern r = Pattern.compile("(\\d+)");
		
	    Elements items = doc.select("item");
	    
	    // 1st item
	    //System.out.println("1st title = " + items.get(0).select("title").first().html());
	    // 2nd item
	    //System.out.println("2nd title = " + items.get(1).select("title").first().html());
	    int indexOfFirstItem = 0;
	    title = items.get(indexOfFirstItem).select("title").first().html();
	    
	    if (title.contains("Турнир") || title.contains("турнир"))
	    {
	    	indexOfFirstItem = 1;
	    }
	    
	    link = items.get(indexOfFirstItem).select("link").first().html();
		Matcher m = r.matcher(link);
		if (m.find( )) 
		{				    
			id = m.group(0);
		}
	    title = items.get(indexOfFirstItem).select("title").first().html();
	    description = items.get(indexOfFirstItem).select("description").first().html();
	    pubDate = items.get(indexOfFirstItem).select("pubDate").first().html();
	    channelName = doc.select("channel > title").first().html();
	    
	    XmlItem lastItem = new XmlItem(title, description, link, id, pubDate, channelName);
	    
    	/*System.out.println("The last item id: " + lastItem.getLink() + ",\n" + 
    											  lastItem.getTitle() + ",\n" +
    								              lastItem.getDescription() + ",\n" +
    								              lastItem.getPubDate() + ",\n" +
    								              lastItem.getChannelName());
		*/
		return lastItem;
	} 
}
