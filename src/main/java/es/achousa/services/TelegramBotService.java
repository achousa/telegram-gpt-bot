/**
 * This class represents a service that handles communication with a Telegram Bot. It receives messages from the bot
 * and processes them accordingly, either executing commands or generating responses using GPT technology.
 */

package es.achousa.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class TelegramBotService {

    protected final Log log = LogFactory.getLog(this.getClass());
    private static final String EMOJI_UFF = "\uD83D\uDE13" ;
    @Autowired
    GptService gptService;

    @Value("${bot.token}")
    private String BOT_TOKEN;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.presentation}")
    private String presentationText;
    private TelegramBot bot;

    /**
     * Initializes the Telegram Bot and sets up the updates listener to receive messages and process them.
     */
    @PostConstruct
    private void init() {

        this.bot = new TelegramBot(BOT_TOKEN);
        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {
                // process updates
                for (Update update : updates) {
                    if (update.message() != null && update.message().text() != null) {
                        if (update.message().text().startsWith("/")) {
                            processCommand(update);
                        } else {
                            // if in a group, only process messages directly addressed to the bot
                            if(isPrivate(update)){
                                processText(update);
                            } else if(update.message().text().toLowerCase().contains("@"+botName.toLowerCase())) {
                                processText(update);
                            }
                        }
                    }
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });

    }

    /**
     * Shuts down the Telegram Bot when the application is terminated.
     */
    @PreDestroy
    private void dispose(){
        log.info("shutting down bot");
        bot.shutdown();
    }

    /**
     * Processes a message received from a Telegram user, generating a response using GPT technology and
     * sending it back.
     * @param update The message received from the user.
     */
    private void processText(Update update) {

        log.info(update.message().from().firstName()+" said ... " + update.message().text());
        String response = this.gptService.SendMessage(update);
        log.info(this.botName + " said ... " + response);
        sendReply(update, response);
    }

    /**
     * Sends a reply message back to the user.
     * @param update The message received from the user.
     * @param message The message to send back to the user.
     */
    private void sendReply(Update update, String message) {
        SendMessage request = new SendMessage(update.message().chat().id(), message)
                .parseMode(ParseMode.Markdown)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyMarkup(new ReplyKeyboardRemove());
        if(!isPrivate(update)){
            // If we are in a group, do a replyTo
            request.replyToMessageId(update.message().messageId());
        }
        // request.replyMarkup(new ForceReply());
        SendResponse sendResponse = bot.execute(request);
        if(!sendResponse.isOk()){
            log.error(sendResponse.message());
        }
    }

    /**
     * Determines whether a message was sent privately to the bot or in a group chat.
     * @param update The message received from the user.
     * @return true if the message is private, false otherwise.
     */
    private boolean isPrivate(Update update) {
        if (update.message().chat().type().equals(Chat.Type.Private)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Processes a command received from a Telegram user, executing the corresponding action.
     * @param update The message received from the user.
     */
    private void processCommand(Update update) {
        if (update.message().text().equalsIgnoreCase("/start")) {
            presentation(update);
        } else if (update.message().text().equalsIgnoreCase("/usage")) {
            printUsage(update);
        } else if (update.message().text().equalsIgnoreCase("/reset")) {
            resetUserContext(update);
        } else {
            log.warn("Unknown command:" +update.message().text());
        }
    }

    /**
     * Sends a custom presentation message to the user.
     * @param update The message received from the user.
     */
    private void presentation(Update update) {
        String response = this.gptService.sendCustomMessage(update, presentationText);
        sendReply(update, response);
    }

    /**
     * Sends a message to the user with information about the number of tokens currently consumed in GPT generation.
     * @param update The message received from the user.
     */
    private void printUsage(Update update) {
        String message = String.format("Contador de tokens: %d",gptService.getNumTokens());
        sendReply(update, message);
    }

    /**
     * Resets the context of a user, clearing message history. Examples will be loaded back when the context
     * gets re-created.
     * @param update The message received from the user.
     */
    private void resetUserContext(Update update) {
        String message = gptService.resetUserContext(update);
        sendReply(update, message);
    }


}
