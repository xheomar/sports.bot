package test.myTelegramBot;

import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import jersey.repackaged.com.google.common.collect.Lists;
import test.myTelegramBot.json.Comment;
import test.myTelegramBot.json.SportsMessages;

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
	private static final String SET_NEW_1 = "/setNew1";
	private static final String SET_NEW_2 = "/setNew2";
	private static final String SET_NEW_3 = "/setNew3";
	private static final String TOP_1 = "/top1";
	private static final String TOP_2 = "/top2";
	private static final String TOP_3 = "/top3";
	private volatile String [] LIST_OF_PUBLICS = {"1412670", "1412898", "1414425"};
	private volatile String [] BLACK_LIST = {""};
	private volatile long [] LIST_OF_LAST_MESSAGES = {0, 0, 0};
	private volatile int [] LIST_OF_STARTS = {0, 0, 0};
	private static final int ADMIN_USERID = 17872630;
	private static final String PUBLIC_CHANNEL = "@systemfootball";
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
		 
		    String id1 = props.getProperty("id1");
		    LIST_OF_PUBLICS[0] = id1;
		    String id2 = props.getProperty("id2");
		    LIST_OF_PUBLICS[1] = id2;
		    String id3 = props.getProperty("id3");
		    LIST_OF_PUBLICS[2] = id3;
		    
		    System.out.println("The following IDs were read: " + id1 + " " + id2 + " " + id3);
		    
		    String blackList = props.getProperty("blackList");
		    BLACK_LIST = blackList.split(",");
		    
		    System.out.print("The following users were muted: ");
		    for (String blacked: BLACK_LIST)
		    {
		    	System.out.print(blacked + " ");
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
					props.setProperty("id1", newPublicId_1);
					saveProperties();
				}
				if (message.getText().startsWith(SET_NEW_2)) 
				{
					String newPublicId_2 = message.getText().split(" ")[1];
					LIST_OF_PUBLICS[1] = newPublicId_2;
					LIST_OF_STARTS[1] = 0;
					sendMsg(message, "New 2nd public is set to " + newPublicId_2);
					props.setProperty("id2", newPublicId_2);
					saveProperties();
				}
				if (message.getText().startsWith(SET_NEW_3)) 
				{
					String newPublicId_3 = message.getText().split(" ")[1];
					LIST_OF_PUBLICS[2] = newPublicId_3;
					LIST_OF_STARTS[2] = 0;
					sendMsg(message, "New 3rd public is set to " + newPublicId_3);
					props.setProperty("id3", newPublicId_3);
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
									Thread.sleep(5000);
									publicId++;
								} 
								catch (InterruptedException e1) 
								{
									e1.printStackTrace();
								}
								
								if (publicId == 3)
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
												sendMessageToChannel(message, app.getMessageFromComment(comment));
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
														sendMessageToChannel(message, app.getMessageFromComment(comment));
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
				else if (message.getText().startsWith("@") && !message.getText().trim().contains(" "))
				{
					sendMessageToChannel(message, message.getText());
				}
				else
				{
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

	private void sendMessageToChannel(Message message, String text) 
	{
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(PUBLIC_CHANNEL);
        sendMessage.setText(text);
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
