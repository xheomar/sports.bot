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

public class App 
{
	private static final String SportsRuUrlTemplate = 
			"http://www.sports.ru/api/comment/get/message.json?message_class=Sports%3A%3ABlog%3A%3APost%3A%3APost&new_time=1&style=newjs&from_id=undefined&";
	//private static final String SportsRuUrlTemplate = "http://www.sports.ru/api/comment/get/message.json?order_type=old&limit=100&message_class=Sports%3A%3ABlog%3A%3APost%3A%3APost&new_time=1&style=newjs&message_id=1055829689";
	   	
	public SportsMessages getSportsMessages(int start, String publicId) 
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = "";		
		if (start == 0)
		{
			url = new String(SportsRuUrlTemplate + "message_id=" + publicId + "&" + "order_type=old&limit=100");	
		}
		else if (start == 1)
		{
			url = new String(SportsRuUrlTemplate + "message_id=" + publicId + "&" + "order_type=new&limit=5");
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
		String text = comment.getText().replaceAll("<br />", "").replaceAll("&quot;", "\""); //.replaceAll("`", "").replaceAll("[", "").replaceAll("]", "");
		String topicCaster = new String("\n#" + comment.getUser().getName().replaceAll(" ", ""));
		String topic = new String("#" + comment.getMessageInfo().getName().replaceAll(" ", "") + 
				" " + '[' + "link" + ']' + '(' + comment.getMessageInfo().getLink() + ')');
		String time = new String(comment.getCTime().getTime() + " " + comment.getCTime().getDate() + "\n");
		if (comment.getAnswerTo() != null)
		{
			String answerText = comment.getAnswerTo().getText().replaceAll("<br />", "").replaceAll("&quot;", "\""); //.replaceAll("_", "").replaceAll("`", "").replaceAll("[", "").replaceAll("]", "");
			String answerTopicCaster = new String(comment.getAnswerTo().getUser().getName().replaceAll(" ", ""));
			answer = new String("\n" + "``` >>> " + answerTopicCaster + ": " + answerText + "```" + "\n");
		}
		
		message = topic + " " + "\n" + time + answer + topicCaster + " :" + "\n*" + text + "*\n";	
		
		return message;
	}

}
