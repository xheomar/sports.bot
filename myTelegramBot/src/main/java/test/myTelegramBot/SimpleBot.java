package test.myTelegramBot;

import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;
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
	private static final String [] LIST_OF_PUBLICS = {"1413705", "1414425", "1414056"};
	private long [] LIST_OF_LAST_MESSAGES = {0, 0, 0};
	private int [] LIST_OF_STARTS = {0, 0, 0};
	private static final int ADMIN_USERID = 17872630;
	private static final String PUBLIC_CHANNEL = "@systemfootball";
	private volatile boolean isRunning = true;
	
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
				telegramBotsApi.registerBot(new SimpleBot(options));
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
				telegramBotsApi.registerBot(new SimpleBot());
			} 
			catch (TelegramApiException e) 
			{
				e.printStackTrace();
			}
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
				if (message.getText() != null && message.getText().equals("/start")) 
				{
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
								
								// Рисуем лог комментов в первый раз
								if (LIST_OF_STARTS[publicId] == 0)
								{								
									for (Comment comment: listComment)
									{
										LIST_OF_LAST_MESSAGES[publicId] = comment.getId();
										sendMessageToChannel(message, app.getMessageFromComment(comment));
										//sendMsg(message, app.getMessageFromComment(comment));
									}
									LIST_OF_STARTS[publicId] += 1;
								}
								else
								{									
									System.out.println("Новые комменты?...");
									// Новые комменты
									boolean allowed = false;
									for (Comment comment: listComment)
									{
										if (!allowed && comment.getId() != LIST_OF_LAST_MESSAGES[publicId])
										{
											// Эти комменты уже были отправлены
											System.out.println(comment.getId() + "!=" + LIST_OF_LAST_MESSAGES[publicId]);
											continue;
										}
										else
										{
											// Это последний отправленный
											if (allowed == false)
											{
												allowed = true;	
											}
											else
											{
												LIST_OF_LAST_MESSAGES[publicId] = comment.getId();
												sendMessageToChannel(message, app.getMessageFromComment(comment));
												//sendMsg(message, app.getMessageFromComment(comment));
											}
											
										}									
									}
									if(!allowed)
									{
										// Если мы не нашли последний отправленный, то он либо
										// удален, либо перекрыт другими. Пофиг, пишем по-новой!
										LIST_OF_STARTS[publicId] = 0;
									}
								}								
							}
							sendMsg(message, "Вы отписались от новостей");
						}
					}.start();
					sendMsg(message, "Я подписал вас на новости");
				}
				else if (message.getText() != null && message.getText().equals("/stop")) 
				{
					isRunning = false;
				}
				// Ждем имя канала, где мы админы
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
			sendMsg(message, "Извините, но вы - не мой админ :(");
		}
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
