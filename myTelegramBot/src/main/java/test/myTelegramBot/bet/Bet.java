package test.myTelegramBot.bet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Bet 
{
	private static final String MaraphoneBetUrlTemplate = 
			"https://www.marathonbet.by/su/betting/Football/England/Northern+Premier+League/Premier+Division/";
	
	private static final String MaraphoneBetMarketsTemplate = 
			"https://www.marathonbet.by/su/markets.htm";
	
	static File configFile = new File("config.properties");
	static Properties props = new Properties();
	
	private volatile static String [] LEAGUES_URL  = {""};
	private volatile static String [] LEAGUE_NAMES = {""};
	
	private static Map<String, String> LEAGUES_ARRAY = new HashMap<String, String>();

	public static void main(String[] args) throws IOException
	{
		getProperties();
		
		for (Map.Entry<String, String> leagueEntry : LEAGUES_ARRAY.entrySet())
		{
			// System.out.println(leagueEntry.getKey() + " " + leagueEntry.getValue());
			Map<String, String> ldsGamesArray = getGames(leagueEntry.getValue());
			if (ldsGamesArray != null)
			{
				int count = 0;
				for (Map.Entry<String, String> games : ldsGamesArray.entrySet())
				{
					if (!games.getValue().equals("null"))
					{
						count++;
					}
				}
				System.out.print(leagueEntry.getKey() + ": " + count + "/");
				System.out.println(ldsGamesArray.size() + " already with AnyOtherScore");
			}
			else
			{
				System.out.println(leagueEntry.getKey() + ": is empty yet");
			}
		}
	}
	
	public static String getBets() throws UnsupportedOperationException, IOException
	{
		getProperties();
		
		String result = "";
		
		for (Map.Entry<String, String> leagueEntry : LEAGUES_ARRAY.entrySet())
		{
			// System.out.println(leagueEntry.getKey() + " " + leagueEntry.getValue());
			Map<String, String> ldsGamesArray = getGames(leagueEntry.getValue());
			if (ldsGamesArray != null)
			{
				int count = 0;
				for (Map.Entry<String, String> games : ldsGamesArray.entrySet())
				{
					if (!games.getValue().equals("null"))
					{
						count++;
					}
				}
				System.out.print(leagueEntry.getKey() + ": " + count + "/");
				result += new String(leagueEntry.getKey() + ": " + count + "/");
				System.out.println(ldsGamesArray.size() + " already with AnyOtherScore");
				result += new String(ldsGamesArray.size() + " already with AnyOtherScore" + "\n");
			}
			else
			{
				System.out.println(leagueEntry.getKey() + ": is empty yet");
				result += new String(leagueEntry.getKey() + ": is empty yet" + "\n");
			}
		}
		
		return result;
	}
	
	public static Map<String, String> getGames(String urlString) throws UnsupportedOperationException, IOException 
	{
		Map<String, String> ldsGamesArray = new HashMap<String, String>();
		
		ArrayList<String> idList = new ArrayList<String>();
		
		// System.out.println("Connecting to " + urlString + "...");	
	
		int count = 0;
		
		try 
		{
			Document doc;
			doc = Jsoup.connect(urlString).timeout(50000).get();	
			Elements games = doc.getElementsByClass("member-area-button");
			
			for (Element game : games) 
			{
				Pattern r = Pattern.compile("\"\\d+\"");
				Matcher m = r.matcher(game.toString());
			    if (m.find( )) 
			    {				
			    	idList.add(m.group(0).replaceAll("\"",""));		
			    	count++;
			    	if (count > 30)
			    	{
			    		return null;
			    	}
			    }
				
			}
		}
		catch (SocketTimeoutException exp)
		{
			System.out.println(urlString + " couldn't be read");
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String id : idList)
		{
			// System.out.println(id);
			
			CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(MaraphoneBetMarketsTemplate);
		 
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("treeId", id));
		    params.add(new BasicNameValuePair("siteStyle", "SIMPLE"));
		    httpPost.setEntity(new UrlEncodedFormEntity(params));
		 
		    httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		    httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		    
		    CloseableHttpResponse response = client.execute(httpPost);
		    
		    int responseCode = response.getStatusLine().getStatusCode();
	    	if (responseCode != 200) 
	    	{
	    		System.out.println("Unexpected response from Sports.Ru: " + responseCode);
	    		return null;
	    	}
	    	else 
	    	{
	    		String responseString = new BasicResponseHandler().handleResponse(response);
	    		//System.out.println(responseString);
	    		
	    		Pattern r = Pattern.compile("AnyOther");
				Matcher m = r.matcher(responseString.toString());
			    if (m.find( )) 
			    {	
			    	ldsGamesArray.put(id, m.group(0));			    	
			    }
			    else
			    {
			    	ldsGamesArray.put(id, "null");	
			    }	    		
	    	}
	    	
		    client.close();
		}
		
		
		/*for (Map.Entry<String, String> entry : ldsGamesArray.entrySet())
		{
			System.out.println(entry.getKey() + " " + entry.getValue());
		}*/
		return ldsGamesArray;
		
	}
	
	private static void getProperties() 
	{
		//System.out.println("Reading configuration... ");
		try 
		{
		    FileReader reader = new FileReader(configFile);
		    
		    props.load(reader);

		    try 
		    {
			    LEAGUES_URL = props.getProperty("leagues").split(" ");
			    LEAGUE_NAMES = props.getProperty("league_names").split(",");
			    
			    for (int i=0; i<LEAGUES_URL.length; i++)
			    {
			    	LEAGUES_ARRAY.put(LEAGUE_NAMES[i], LEAGUES_URL[i]);
			    }
			    
			    
		    }
		    catch (NullPointerException exp)
		    {
		    	System.out.print("Configuration Error");
		    }
		    
		    reader.close();
		} 
		catch (FileNotFoundException ex) 
		{
		    System.out.println("File Not Found");
		} 
		catch (IOException ex) 
		{
			System.out.println("I/O error");
		}
	}
	
}
