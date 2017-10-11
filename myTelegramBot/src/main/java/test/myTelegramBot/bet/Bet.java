package test.myTelegramBot.bet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
	
	static File configFile = new File("config.properties");
	static Properties props = new Properties();
	
	private volatile static String [] LEAGUES_URL  = {""};
	private volatile static String [] LEAGUE_NAMES = {""};
	
	private static Map<String, String> LEAGUES_ARRAY = new HashMap<String, String>();

	public static void main(String[] args) throws IOException
	{
		getBets();
	}
	
	public static String getBets() throws UnsupportedOperationException, IOException
	{
		getProperties();
		
		String resultOverall = "";
		
		LEAGUES_ARRAY = sortByValues(LEAGUES_ARRAY, -1);
		
		for (Map.Entry<String, String> leagueEntry : LEAGUES_ARRAY.entrySet())
		{
			String result = "";
			
			// System.out.println(leagueEntry.getKey() + " " + leagueEntry.getValue());
			Map<String, String> ldsGamesArray = getGames(leagueEntry.getValue());
			if (ldsGamesArray != null)
			{
				ldsGamesArray = sortByValues(ldsGamesArray, -1);
				
				int count = 0;
				for (Map.Entry<String, String> games : ldsGamesArray.entrySet())
				{
					if (!games.getValue().startsWith("null"))
					{
						count++;
					}
				}
				result += new String(leagueEntry.getKey() + ": " + count + "/" + ldsGamesArray.size());
				
				if (count != 0)
				{
					result += new String(": {");
					
					for (Map.Entry<String, String> games : ldsGamesArray.entrySet())
					{
						if (!games.getValue().startsWith("null"))
						{
							String gameBet = new String("*" + games.getValue().split(",")[1] + "*");
							String gameDate = new String(" (" + games.getValue().split(",")[0] + ") ");
							//String leagueLink = new String(" [" + gameDate + "]" + " (" + MaraphoneBetUrlTemplate + leagueEntry.getValue() + ") ");
							
							// result += new String(" " + gameBet + leagueLink);
							result += new String(" " + gameBet + gameDate);
						}
					}
					
					result += new String("}");
				}
				
				result += new String("\n\n");
			}
			else
			{
				//result += new String(leagueEntry.getKey() + ": is empty yet" + "\n");
			}
			//break;
			System.out.print(result);
			resultOverall += result;
		}
		
		System.out.print(resultOverall);
		
		return resultOverall;
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
			doc = Jsoup.connect(MaraphoneBetUrlTemplate + urlString).timeout(50000).get();	
			// Elements games = doc.getElementsByClass("member-area-button");
			Elements games = doc.select("td[class = member-area-button]");
			Elements dates = doc.getElementsByClass("date ");
			
			/*for (Element game : games) 
			{
				// System.out.println(game);
				Pattern r = Pattern.compile("\"\\d+\"");
				Matcher m = r.matcher(game.toString());
			    if (m.find( )) 
			    {				
			    	idList.add(m.group(0).replaceAll("\"",""));		
			    	System.out.println(m.group(0).replaceAll("\"",""));
			    	count++;
			    	if (count > 30)
			    	{
			    		return null;
			    	}
			    }
			}*/
			
			//System.out.println(games.size());
			//System.out.println(dates.size());
			
			/*for (Element date : dates)
			{
				System.out.println(date.html());
			}
			
			for (Element game : games)
			{
				System.out.println(game.html());
			}*/
			
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
	    		Pattern r = Pattern.compile("AnyOther\">[0-9]+(.[0-9]+)?");
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
			    	ldsGamesArray.put(id, new String("null" + "," + gameDate));	
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
		//System.out.println("Reading configuration... ");
		try 
		{
		    FileReader reader = new FileReader(configFile);
		    
		    props.load(reader);

		    try 
		    {
			    LEAGUES_URL = props.getProperty("leagues").split(" ");
			    LEAGUE_NAMES = props.getProperty("league_names").split(",");
			    
			    LEAGUES_ARRAY.clear();
			    
			    for (int i=0; i<LEAGUES_URL.length; i++)
			    {
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
	    Comparator<K> valueComparator =  new Comparator<K>() {         
	       private int ascending;
	       public int compare(K k1, K k2) {
	           int compare = map.get(k2).compareTo(map.get(k1));
	           if (compare == 0) return 1;
	           else return ascending*compare;
	       }
	       public Comparator<K> setParam(int ascending)
	       {
	           this.ascending = ascending;
	           return this;
	       }
	   }.setParam(ascending);
	
	   Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	   sortedByValues.putAll(map);
	   return sortedByValues;
	}
	
}
