package test.myTelegramBot;

import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

import jersey.repackaged.com.google.common.collect.Lists;
import test.myTelegramBot.bet.Bet;
import test.myTelegramBot.json.Comment;
import test.myTelegramBot.json.SportsMessages;
import test.myTelegramBot.xml.XmlItem;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class SimpleBot extends TelegramLongPollingBot 
{	
	private static final String ADMIN_USERNAME = "Heorhi";
	private static final String SET_NEW_1 = "/set1";
	private static final String SET_NEW_2 = "/set2";
	private static final String SET_NEW_3 = "/set3";
	private static final String SET_NEW_4 = "/set4";
	private static final String TOP_1 = "/top1";
	private static final String TOP_2 = "/top2";
	private static final String TOP_3 = "/top3";
	private static final String TOP_4 = "/top4";
	private volatile String [] LIST_OF_PUBLICS = {"", "", "", "", ""};
	private volatile String [] LIST_OF_NAMES = {"", "", "", "", ""};
	private volatile String [] BLACK_LIST = {""};
	private volatile String [] WHITE_LIST = {""};
	private volatile long [] LIST_OF_LAST_MESSAGES = {0, 0, 0, 0, 0};
	private volatile int [] LIST_OF_STARTS = {0, 0, 0, 0, 0};
	private static final int ADMIN_USERID = 17872630;
	private static final String PUBLIC_CHANNEL = "@systemfootball";
	private static final String THE_BEST_PUBLIC_CHANNEL = "@sportsruthebest";
	private static final String SUBBSCRIPTIONS_PUBLIC_CHANNEL = "@sportsrusubscriptions";
	private volatile boolean isRunning = true;
	private volatile boolean isDebug = false; 
	
	static File configFile = new File("config.properties");
	static Properties props = new Properties();
	
	private SimpleBot(DefaultBotOptions options) 
	{
        super(options);
    }
	
	private SimpleBot() 
	{
        super();
    }
 
	public static void main(String[] args) 
	{
		boolean isProxyNeeded = false;
		if (isProxyNeeded)
		{
			HttpHost proxy = new HttpHost("172.22.4.1", 8080);
		    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		    DefaultBotOptions options = new DefaultBotOptions();
		    options.setRequestConfig(config);
		    
			ApiContextInitializer.init();
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			try 
			{ 
				SimpleBot myBot = new SimpleBot(options);
				System.out.println("Bot registration... ");
				telegramBotsApi.registerBot(myBot);
				myBot.getProperties();
			} 
			catch (TelegramApiException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			ApiContextInitializer.init();
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			try 
			{ 
				SimpleBot myBot = new SimpleBot();
				System.out.println("Bot registration... ");
				telegramBotsApi.registerBot(myBot);
				myBot.getProperties();
				
				System.out.println("\n\nBot is running! ");
			} 
			catch (TelegramApiException e) 
			{
				e.printStackTrace();
			}
		}
	}
 

	private void getProperties() 
	{
		System.out.println("Reading configuration... ");
		try 
		{
		    FileReader reader = new FileReader(configFile);
		    
		    props.load(reader);
		 
		    String id0 = props.getProperty("id0");
		    LIST_OF_PUBLICS[0] = id0;
		    String id1 = props.getProperty("id1");
		    LIST_OF_PUBLICS[1] = id1;
		    String id2 = props.getProperty("id2");
		    LIST_OF_PUBLICS[2] = id2;
		    String id3 = props.getProperty("id3");
		    LIST_OF_PUBLICS[3] = id3;
		    String id4 = props.getProperty("id4");
		    LIST_OF_PUBLICS[4] = id4;
		    
		    System.out.println("The following IDs were read: " + id0 + " " + id1 + " " + id2 + " " + id3 + " " + id4);
		    
		    String name0 = props.getProperty("name0");
		    LIST_OF_NAMES[0] = name0;
		    String name1 = props.getProperty("name1");
		    LIST_OF_NAMES[1] = name1;
		    String name2 = props.getProperty("name2");
		    LIST_OF_NAMES[2] = name2;
		    String name3 = props.getProperty("name3");
		    LIST_OF_NAMES[3] = name3;
		    String name4 = props.getProperty("name4");
		    LIST_OF_NAMES[4] = name4;
		    
		    System.out.println("The following names were read: " + name0 + " " + name1 + " " + name2 + " " + name3 + " " + name4);
		    
		    String blackList = props.getProperty("blackList");
		    BLACK_LIST = blackList.split(",");
		    
		    System.out.print("The following users were muted: ");
		    for (String blacked: BLACK_LIST)
		    {
		    	System.out.print(blacked + " ");
		    }
		    
		    System.out.print("\n\n");
		    
		    String whiteList = props.getProperty("whiteList");
		    WHITE_LIST = whiteList.split(",");
		    
		    System.out.print("The following users were added to white list: ");
		    for (String whites: WHITE_LIST)
		    {
		    	System.out.print(whites + " ");
		    }
		    
		    System.out.print("\n\n");
		    
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

	private static void saveProperties() 
	{
		System.out.println("Saving configuration... ");
		try 
		{
			FileWriter writer = new FileWriter(configFile);
			props.store(writer, "bot settings");
			writer.close();
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
	
	public String getBotUsername() 
	{
		return "SystemFootballBot";
	}
 
	@Override
	public String getBotToken() 
	{
		return "420556647:AAFmx0zJGZc0YKQUstPHONkmIFgeiKOtgUI";
	}
 

	public void onUpdateReceived(Update update) 
	{
		final Message message = update.getMessage();
		if (message.getFrom().getUserName().equals(ADMIN_USERNAME) && message.getFrom().getId() == ADMIN_USERID)
		{
			if (message != null && message.hasText()) 
			{
				System.out.println("Received the message: " + message.getText());
				
				if (message.getText().startsWith(SET_NEW_1)) 
				{
					String newPublicId_1 = message.getText().split(" ")[1];
					LIST_OF_PUBLICS[0] = newPublicId_1;
					LIST_OF_STARTS[0] = 0;
					sendMsg(message, "New 1st public is set to " + newPublicId_1);
					props.setProperty("id0", newPublicId_1);
					saveProperties();
				}
				if (message.getText().startsWith(SET_NEW_2)) 
				{
					String newPublicId_2 = message.getText().split(" ")[1];
					LIST_OF_PUBLICS[1] = newPublicId_2;
					LIST_OF_STARTS[1] = 0;
					sendMsg(message, "New 2nd public is set to " + newPublicId_2);
					props.setProperty("id1", newPublicId_2);
					saveProperties();
				}
				if (message.getText().startsWith(SET_NEW_3)) 
				{
					String newPublicId_3 = message.getText().split(" ")[1];
					LIST_OF_PUBLICS[2] = newPublicId_3;
					LIST_OF_STARTS[2] = 0;
					sendMsg(message, "New 3rd public is set to " + newPublicId_3);
					props.setProperty("id2", newPublicId_3);
					saveProperties();
				}
				if (message.getText().startsWith(SET_NEW_4)) 
				{
					String newPublicId_4 = message.getText().split(" ")[1];
					LIST_OF_PUBLICS[3] = newPublicId_4;
					LIST_OF_STARTS[3] = 0;
					sendMsg(message, "New 4th public is set to " + newPublicId_4);
					props.setProperty("id3", newPublicId_4);
					saveProperties();
				}
				
				if (message.getText().startsWith(TOP_1) || message.getText().startsWith(TOP_2) || message.getText().startsWith(TOP_3)) 
				{
					App app = new App();
					int id = Integer.parseInt(message.getText().replaceAll("/top", ""));
					SportsMessages topMessages = app.getSportsMessages(2, LIST_OF_PUBLICS[id-1]);
					List<Comment> listComment = (List) Lists.reverse(topMessages.getData().getComments());
					String topComments = "";
					
					for (Comment comment: listComment)
					{
						topComments += app.getTopComments(comment);
						topComments += "\n";
					}
					sendMsg(message, topComments);
				}
				
				if (message.getText().equals("/start") || message.getText().equals("/startDebug")) 
				{
					if (message.getText().equals("/start")) isDebug = false;
					if (message.getText().equals("/startDebug")) isDebug = true;
					isRunning = true;
					new Thread() 
					{
						@Override
						public void run()
						{
							int publicId=0;
							while (isRunning) 
							{
								try 
								{
									Thread.sleep(10000);
									publicId++;
								} 
								catch (InterruptedException e1) 
								{
									e1.printStackTrace();
								}
								
								if (publicId == 5)
								{
									publicId = 0;
								}
								
								App app = new App();
								SportsMessages messages = app.getSportsMessages(1, LIST_OF_PUBLICS[publicId]);
								if (messages == null || messages.getData() == null || messages.getData().getComments() == null)
								{
									continue;
								}
								List<Comment> listComment = (List) Lists.reverse(messages.getData().getComments());
								
								if (LIST_OF_STARTS[publicId] == 0)
								{								
									for (Comment comment: listComment)
									{
										if (!isBlackListed(comment))
										{
											LIST_OF_LAST_MESSAGES[publicId] = comment.getId();
											if (isDebug)
											{
												sendMsg(message, app.getMessageFromComment(comment));
											}
											else
											{
												sendMessageToChannel(PUBLIC_CHANNEL, message, app.getMessageFromComment(comment));
												if (isWhiteListed(comment))
												{
													sendMessageToChannel(THE_BEST_PUBLIC_CHANNEL, message, app.getMessageFromComment(comment));
												}
											}	
										}
									}
									LIST_OF_STARTS[publicId] += 1;
								}
								else
								{									
									System.out.println("New comments?...");
									boolean allowed = false;
									for (Comment comment: listComment)
									{
										if (!isBlackListed(comment))
										{
											if (!allowed && comment.getId() != LIST_OF_LAST_MESSAGES[publicId])
											{
												System.out.println(comment.getId() + "!=" + LIST_OF_LAST_MESSAGES[publicId]);
												continue;
											}
											else
											{
												if (allowed == false)
												{
													allowed = true;	
												}
												else
												{
													LIST_OF_LAST_MESSAGES[publicId] = comment.getId();
													if (isDebug)
													{
														sendMsg(message, app.getMessageFromComment(comment));
													}
													else
													{
														sendMessageToChannel(PUBLIC_CHANNEL, message, app.getMessageFromComment(comment));
														
														if (isWhiteListed(comment))
														{
															sendMessageToChannel(THE_BEST_PUBLIC_CHANNEL, message, app.getMessageFromComment(comment));
														}
													}
												}
											}
										}
									}
									if(!allowed)
									{
										LIST_OF_STARTS[publicId] = 0;
									}
								}	
								// Check new subscriptions
								try 
								{
									getProperties();
									XmlItem item = XmlReader.getRssFeedLastItemId(LIST_OF_NAMES[publicId]);
									System.out.println("item.getId(): " + item.getId());
									System.out.println("LIST_OF_PUBLICS[i]: " + LIST_OF_PUBLICS[publicId] + "\n");
									if (!item.getId().equals(LIST_OF_PUBLICS[publicId]))
									{
										props.setProperty(new String("id" + publicId), item.getId());
										saveProperties();
										if (isDebug)
										{
											sendMsg(message, App.getActiveSubscriptions(item));
										}
										else
										{
											sendMessageToChannel(SUBBSCRIPTIONS_PUBLIC_CHANNEL, message, App.getActiveSubscriptions(item));
											sendMessageToChannel(THE_BEST_PUBLIC_CHANNEL, message, App.getActiveSubscriptions(item));
										}
										LIST_OF_STARTS[publicId] = 0;
										getProperties();
									}
								} 
								catch (IOException e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							sendMsg(message, "You has been unsubscribed");
						}
					}.start();
					sendMsg(message, "You has been subscribed");
				}
				else if (message.getText() != null && message.getText().equals("/stop")) 
				{
					isRunning = false;
				}
				else if (message.getText().equals("/printActive"))
				{
					getProperties();
					for (int i = 0; i < LIST_OF_NAMES.length; i++)
					{
						try 
						{
							XmlItem item = XmlReader.getRssFeedLastItemId(LIST_OF_NAMES[i]);
							System.out.println("item.getId(): " + item.getId());
							System.out.println("item.getTitle(): " + item.getTitle());
							System.out.println("LIST_OF_PUBLICS[i]: " + LIST_OF_PUBLICS[i] + "\n");
							if (!item.getId().equals(LIST_OF_PUBLICS[i]) && !item.getTitle().equalsIgnoreCase("ТУРНИР"))
							{
								props.setProperty(new String("id" + i), item.getId());
								saveProperties();
								if (isDebug)
								{
									sendMsg(message, App.getActiveSubscriptions(item));
								}
								else
								{
									//sendMessageToChannel(SUBBSCRIPTIONS_PUBLIC_CHANNEL, message, App.getActiveSubscriptions(item));
									sendMsg(message, App.getActiveSubscriptions(item));
									//sendMessageToChannel(THE_BEST_PUBLIC_CHANNEL, message, App.getActiveSubscriptions(item));
								}
								getProperties();
							}
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else if (message.getText().equals("/printBets"))
				{
					new Thread() 
					{
						@Override
						public void run()
						{
							try 
							{
								sendMsg(message, "Calculating AnyOtherScore bets is in progress...");
								LinkedList<String> resultQueue = Bet.getBets();
								if (resultQueue == null || resultQueue.size() == 0)
								{
									sendMsg(message, "Something goes wrong");
								}
								else
								{
									DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									Date date = new Date();
									//System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
									String today = new String(dateFormat.format(date) + "\n\n");
									
									int pages = resultQueue.size() / 10;
									if (resultQueue.size() % 10 != 0) pages++;
									
									String portion10 = "";
									for (int i = 0, j = 0; i < resultQueue.size(); i++)
									{
										portion10 += resultQueue.get(i);
										System.out.println(i);
										if ((i!=0 && i % 10 == 0) || i == resultQueue.size()-1)
										{
											sendMsg(message, today + "Part " + ++j + "/" 
															+ pages + "\n\n" + portion10);
											portion10 = "";
										}
									}
								}
							} 
							catch (UnsupportedOperationException e) 
							{
								sendMsg(message, "Something goes wrong");
								e.printStackTrace();
							} 
							catch (IOException e) 
							{
								sendMsg(message, "Something goes wrong");
								e.printStackTrace();
							}
						}
					}.start();
				}
				else
				{
					sendMsg(message, "/start /startDebug /printBets /printActive /stop");
					System.out.println(message.getFrom().getUserName() + " " + message.getFrom().getId());
				}
			}	
		}
		else 
		{
			sendMsg(message, "You are not my admin :(");
		}
	}
	
	protected boolean isBlackListed(Comment comment) 
	{
	    for (String blacked: BLACK_LIST)
	    {
	    	if (comment.getUser().getName().equalsIgnoreCase(blacked) ||
	    			(comment.getAnswerTo() != null && comment.getAnswerTo().getUser().getName().equalsIgnoreCase(blacked)))
			{
				return true;
			}
	    }
		return false;
	}
	
	protected boolean isWhiteListed(Comment comment) 
	{
	    for (String whites: WHITE_LIST)
	    {
	    	if (comment.getUser().getName().equalsIgnoreCase(whites))
			{
				return true;
			}
	    }
		return false;
	}

	private void sendMessageToChannel(String channelAddress, Message message, String text) 
	{
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(channelAddress);
        sendMessage.setText(text);
        if (channelAddress.equals(THE_BEST_PUBLIC_CHANNEL) || channelAddress.equals(PUBLIC_CHANNEL))
        {
        	sendMessage.disableWebPagePreview();
        }
        System.out.println("Sent the message: " + text);
        try 
        {
            sendMessage(sendMessage);
        } 
        catch (TelegramApiException e) 
        {
            //sendErrorMessage(message, e.getMessage());
        }
    }
 
	private void sendMsg(Message message, String text) 
	{
		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(true);
		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setText(text);
		sendMessage.disableWebPagePreview();
		System.out.println("Sent the message: " + text);
		try 
		{
			sendMessage(sendMessage);
		} catch (TelegramApiException e) 
		{
			e.printStackTrace();
		}
	}
}
