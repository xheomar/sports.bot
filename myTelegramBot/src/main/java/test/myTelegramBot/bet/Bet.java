package test.myTelegramBot.bet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.TreeMap;
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

import com.google.common.collect.MultimapBuilder.SortedSetMultimapBuilder;

public class Bet 
{
	private static final String MaraphoneBetMarketsTemplate = 
			"https://www.marathonbet.by/su/markets.htm";
	
	private static final String MaraphoneBetUrlTemplate = 
			"https://www.marathonbet.by/su/betting/Football/";
	
	private static final String BOLD = "*";
	private static final String ITALIC = "_";
	
	static File configFile = new File("config.properties");
	static Properties props = new Properties();
	
	private volatile static String [] LEAGUES_URL  = {""};
	private volatile static String [] LEAGUE_NAMES = {""};
	private volatile static String [] EVENTS = {""};
	
	private static Map<String, String> LEAGUES_ARRAY = new HashMap<String, String>();

	public static void main(String[] args) throws IOException
	{
		getBets();
	}
	
	public static LinkedList<String> getBets() throws UnsupportedOperationException, IOException
	{
		getProperties();

		LinkedList<String> resultQueue = new LinkedList<String>();
		
		if (LEAGUES_ARRAY != null)
		{
			LEAGUES_ARRAY = MapUtil.sortByValue(LEAGUES_ARRAY);
		}	
		else 
		{
			System.out.println("LEAGUES_ARRAY == null"); 
		}
		
		int eventId = 0;
		for (Map.Entry<String, String> leagueEntry : LEAGUES_ARRAY.entrySet())
		{
			String result = "";
			
			// System.out.println(leagueEntry.getKey() + " " + leagueEntry.getValue());
			Map<String, String> ldsGamesArray = getGames(leagueEntry.getValue(), EVENTS[eventId]);
			if (ldsGamesArray != null)
			{
				ldsGamesArray = MapUtil.sortByValue(ldsGamesArray);
				int count = 0;
				for (Map.Entry<String, String> games : ldsGamesArray.entrySet())
				{
					if (!games.getValue().startsWith("null"))
					{
						count++;
					}
				}	
				
				result += new String("[" + leagueEntry.getKey() + "](" + MaraphoneBetUrlTemplate + leagueEntry.getValue() + ") : " + count + "/" + ldsGamesArray.size() 
																					+ " *" + EVENTS[eventId] + "*");
				eventId++;
				result += new String(" : \n");
				
				String todayGames = "", notTodayGames = "";
				for (Map.Entry<String, String> games : ldsGamesArray.entrySet())
				{
					if (!games.getValue().startsWith("null"))
					{
						
						String gameBet, gameDate;
						
						gameDate = " (" + games.getValue().split(",")[0] + ") ";
						
						// TODAY?
						if (!games.getValue().split(",")[0].contains("."))
						{
							gameBet = toBold(games.getValue().split(",")[1]);
							todayGames += gameBet + gameDate;
						}
						// NOT TODAY
						else
						{
							gameBet = toItalic(games.getValue().split(",")[1]);
							notTodayGames += gameBet + gameDate;
						}		
					}
				}

				for (Map.Entry<String, String> games : ldsGamesArray.entrySet())
				{
					if (games.getValue().startsWith("null"))
					{
						String gameBet, gameDate;
						
						gameDate = " (" + games.getValue().split(",")[1] + ") ";
						
						// TODAY?
						if (!games.getValue().split(",")[1].contains("."))
						{
							gameBet = toBold("?");
							todayGames += gameBet + gameDate;
						}
						// NOT TODAY
						else
						{
							gameBet = toItalic("?");
							notTodayGames += gameBet + gameDate;
						}
					}
				}
				
				if (!todayGames.isEmpty())
				{
					result += todayGames + "\n";
				}
				if (!notTodayGames.isEmpty())
				{
					result += notTodayGames + "\n";
				}
				result += new String("\n");
			}
			else
			{
				result += new String("[" + leagueEntry.getKey() + "](" + MaraphoneBetUrlTemplate + leagueEntry.getValue() + ") : is empty yet" + "\n\n");
			}
			System.out.print(result);
			
			resultQueue.add(result);
		}

		return resultQueue;
	}
	
	private static String toBold(String string) 
	{
		return new String(BOLD + string + BOLD);
	}
	
	private static String toItalic(String string) 
	{
		return new String(ITALIC + string + ITALIC);
	}

	public static Map<String, String> getGames(String urlString, String event) throws UnsupportedOperationException, IOException 
	{
		Map<String, String> ldsGamesArray = new HashMap<String, String>();
		
		ArrayList<String> idList = new ArrayList<String>();
		
		// System.out.println("Connecting to " + urlString + "...");	
	
		int count = 0;
		
		try 
		{
			Document doc;
			doc = Jsoup.connect(MaraphoneBetUrlTemplate + urlString).timeout(50000).get();	
			// Elements games = doc.getElementsByClass("member-area-button");
			Elements games = doc.select("td[class = member-area-button]");
			Elements dates = doc.getElementsByClass("date ");

			for (int i = 1, j = 0; i < games.size(); i+=2, j++)
			{
				String gameId = "";
				String gameDate = "";
				Pattern r = Pattern.compile("\"\\d+\"");
				Matcher m = r.matcher(games.get(i).toString());
			    if (m.find( )) 
			    {				
			    	gameId = new String(m.group(0).replaceAll("\"",""));
			    	gameDate = new String(dates.get(j).html());
			    	idList.add(gameId + "," + gameDate);		
			    	//System.out.println(gameId + "," + gameDate);
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
			
			String gameId = new String(id.split(",")[0]);
			String gameDate = new String(id.split(",")[1]);
			
			CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(MaraphoneBetMarketsTemplate);
		 
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("treeId", gameId));
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
	    		responseString = responseString.replaceAll("\\\\n","").replaceAll("\\\\","");
	    		//System.out.println(responseString);
	    		//Pattern r = Pattern.compile("AnyOther\">[0-9]+(.[0-9]+)?");
	    		Pattern r = Pattern.compile(event +"\">[0-9]+(.[0-9]+)?");
	    		// Pattern r = Pattern.compile("AnyOther");
				Matcher m = r.matcher(responseString.toString());
			    if (m.find( )) 
			    {	
			    	String bet = m.group(0).split(">")[1];
			    	//System.out.println(bet);
			    	ldsGamesArray.put(id, new String(changeDate(gameDate) + "," + bet));			    	
			    }
			    else
			    {
			    	ldsGamesArray.put(id, new String("null" + "," + changeDate(gameDate)));	
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
	
	private static String changeDate(String gameDate) 
	{
		String newDate = gameDate.
				replaceAll(" окт", ".10");
		return newDate;
	}

	private static void getProperties() 
	{
		System.out.println("Reading configuration... ");
		try 
		{
		    FileReader reader = new FileReader(configFile);
		    
		    props.load(reader);

		    try 
		    {
			    LEAGUES_URL = props.getProperty("leagues").split(" ");
			    LEAGUE_NAMES = props.getProperty("league_names").split(",");
			    EVENTS = props.getProperty("events").split(" ");
			    
			    LEAGUES_ARRAY.clear();
			    
			    for (int i=0; i<LEAGUES_URL.length; i++)
			    {
			    	//System.out.println(LEAGUE_NAMES[i].trim() + "  " + LEAGUES_URL[i]);
			    	LEAGUES_ARRAY.put(LEAGUE_NAMES[i].trim(), LEAGUES_URL[i]);
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
	
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map, int ascending)
	{
	    Comparator<K> valueComparator =  new Comparator<K>() 
	    {         
	       private int ascending;
	       public int compare(K k1, K k2) 
	       {
	    	   System.out.println("map.get(k1) = " + map.get(k1));
	    	   System.out.println("map.get(k2) = " + map.get(k2));
	           int compare = map.get(k2).compareTo(map.get(k1));
	           if (compare == 0) 
	        	   return 1;
	           else 
	        	   return ascending*compare;
	       }
	       public Comparator<K> setParam(int ascending)
	       {
	           this.ascending = ascending;
	           return this;
	       }
	   }.setParam(ascending);
	
	   Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	   try
	   {
		   sortedByValues.putAll(map);
	   }
	   catch (NullPointerException e)
	   {
		   e.printStackTrace();
		   System.out.println(e.getMessage());
		   
		   System.out.println("valueComparator = " + valueComparator);
		   System.out.println("sortedByValues = " + sortedByValues);
		   System.out.println("map = " + map);
	   }
	   
	   return sortedByValues;
	}
	
}
