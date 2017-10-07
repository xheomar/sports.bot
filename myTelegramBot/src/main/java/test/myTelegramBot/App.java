package test.myTelegramBot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import test.myTelegramBot.json.Comment;
import test.myTelegramBot.json.SportsMessages;
import test.myTelegramBot.xml.XmlItem;

public class App 
{
	private static final String SportsRuUrlTemplate = 
			"http://www.sports.ru/api/comment/get/message.json?message_class=Sports%3A%3ABlog%3A%3APost%3A%3APost&new_time=1&style=newjs&from_id=undefined&";
	//private static final String SportsRuUrlTemplate = "http://www.sports.ru/api/comment/get/message.json?order_type=old&limit=100&message_class=Sports%3A%3ABlog%3A%3APost%3A%3APost&new_time=1&style=newjs&message_id=1055829689";
	   	
	/*
	 * KEY = 0: 100 oldest messages
	 * KEY = 1: 5 newest messages
	 * KEY = 2: TOP-10 messages 
	 */
	public SportsMessages getSportsMessages(int key, String publicId) 
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = "";		
		if (key == 0)
		{
			url = new String(SportsRuUrlTemplate + "message_id=" + publicId + "&" + "order_type=old&limit=100");	
		}
		else if (key == 1)
		{
			url = new String(SportsRuUrlTemplate + "message_id=" + publicId + "&" + "order_type=new&limit=5");
		}
		else if (key == 2)
		{
			url = new String(SportsRuUrlTemplate + "message_id=" + publicId + "&" + "order_type=top10&limit=10");
		}
		
		System.out.println("Connecting to " + url + "...");	
		
		boolean isProxyNeeded = false;
		HttpGet request = new HttpGet(url);
		
		if (isProxyNeeded)
		{
			HttpHost proxy = new HttpHost("172.22.4.1", 8080);
			RequestConfig config = RequestConfig.custom()
	                .setProxy(proxy)
	                .build();
		
			request.setConfig(config);
		}
			
		CloseableHttpResponse response;
		try 
		{
			response = httpClient.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
	    	if (responseCode != 200) 
	    	{
	    		System.out.println("Unexpected response from Sports.Ru: " + responseCode);
	    		return null;
	    	}
	    	else 
	    	{
	    		//System.out.println("Expected response from Sports.Ru: " + responseCode);
	    		//System.out.println(response.toString());
	    		InputStream source = response.getEntity().getContent();
                Reader reader = new InputStreamReader(source);
                Gson gson = new Gson();
                SportsMessages messages = gson.fromJson(reader, SportsMessages.class);
                System.out.println(messages.getStatus());                               
        		return messages;
	    	}
		} 
		catch (ClientProtocolException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public String getMessageFromComment(Comment comment)
	{
		String message = "";
		String answer = "";
		String hashTagAnswerTopicCaster = "";
		String text = comment.getText().replaceAll("<br />", "").replaceAll("&quot;", "\""); //.replaceAll("`", "").replaceAll("[", "").replaceAll("]", "");
		// delete all hrefs
		text = text.replaceAll("<a(.*?)>", "").replaceAll("<\\/a>", "");
		String topicCaster = new String(comment.getUser().getName().replaceAll("_", ""));
		String hashTagTopicCaster = new String("#" + comment.getUser().getName().replaceAll(" ", "").replaceAll("_", "").replaceAll("-", ""));
		String hashTagTopic = new String("#" + comment.getMessageInfo().getName().replaceAll(" ", "") + 
				" " + '[' + "link" + ']' + '(' + comment.getMessageInfo().getLink() + ')');
		String time = new String(comment.getCTime().getTime() + " " + comment.getCTime().getDate());
		if (comment.getAnswerTo() != null)
		{
			String answerText = comment.getAnswerTo().getText().replaceAll("<br />", "").replaceAll("&quot;", "\""); //.replaceAll("_", "").replaceAll("`", "").replaceAll("[", "").replaceAll("]", "");
			String answerTopicCaster = new String(comment.getAnswerTo().getUser().getName().replaceAll(" ", "").replaceAll("_", "").replaceAll("-", ""));
			hashTagAnswerTopicCaster = new String("#" + answerTopicCaster);
			answer = new String("``` >>> " + answerTopicCaster + ": " + answerText + "```" + "\n");
		}
		
		// message = topic + " " + "\n" + time + answer + topicCaster + " :" + "\n*" + text + "*\n";
		
		message = "*" + topicCaster + ": " + text + "*\n" + answer + "\n" + time + " " + hashTagTopicCaster + " "  + hashTagAnswerTopicCaster + " " + hashTagTopic;
		
		return message;
	}

	public String getTopComments(Comment comment) 
	{
		String message = "";
		String answer = "";
		String text = comment.getText().replaceAll("<br />", "").replaceAll("&quot;", "\""); //.replaceAll("`", "").replaceAll("[", "").replaceAll("]", "");
		// delete all hrefs
		text = text.replaceAll("<a(.*?)>", "").replaceAll("<\\/a>", "");
		String topicCaster = new String(comment.getUser().getName().replaceAll("_", ""));
		String time = new String(comment.getCTime().getTime() + " " + comment.getCTime().getDate());
		String rating = new String("+" + comment.getRating().getPlus() + " -" + comment.getRating().getMinus());
		if (comment.getAnswerTo() != null)
		{
			String answerText = comment.getAnswerTo().getText().replaceAll("<br />", "").replaceAll("&quot;", "\""); //.replaceAll("_", "").replaceAll("`", "").replaceAll("[", "").replaceAll("]", "");
			String answerTopicCaster = new String(comment.getAnswerTo().getUser().getName().replaceAll(" ", "").replaceAll("_", ""));
			answer = new String("``` >>> " + answerTopicCaster + ": " + answerText + "```" + "\n");
		}
		
		message = "*" + topicCaster + ":*\n" 
				+ rating + " " + time + "\n"	  
				+ text + "\n" + answer + "\n";
		
		return message;
	}
	
	public static String getActiveSubscriptions(XmlItem item) 
	{
		String message = "";

		String channelName = new String(item.getChannelName() + "\n");
		String link = new String('[' + "link" + ']' + '(' + item.getLink() + ')' + "\n");
		String time = new String(item.getPubDate() + "\n");
		String title = new String("*" + item.getTitle() + "*\n");
		String description = new String("``` >>> " + item.getDescription() + "```" + "\n"); 
		
		message = channelName + title + description + time + link;
		
		return message;
	}

}
