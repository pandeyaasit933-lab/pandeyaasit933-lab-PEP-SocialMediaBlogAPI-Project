package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;

import Service.AccountService;
import Service.MessageService;

import java.util.List;

public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {

        Javalin app = Javalin.create();

        // Register account
        app.post("/register", this::registerHandler);

        // Login account
        app.post("/login", this::loginHandler);

        // Create message
        app.post("/messages", this::createMessageHandler);

        // Retrieve all messages
        app.get("/messages", this::getAllMessagesHandler);

        // Retrieve message by ID
        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        // Delete message
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        // Update message
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        // Retrieve messages by account ID
        app.get("/accounts/{account_id}/messages",
                this::getMessagesByAccountHandler);

        return app;
    }

    // 1. Register account
    private void registerHandler(Context context) {

        try {
            Account account = context.bodyAsClass(Account.class);

            Account registeredAccount =
                    accountService.registerAccount(account);

            if (registeredAccount != null) {
                context.status(200).json(registeredAccount);
            } else {
                context.status(400);
            }

        } catch (Exception e) {
            context.status(400);
        }
    }

    // 2. Login account
    private void loginHandler(Context context) {

        try {
            Account account = context.bodyAsClass(Account.class);

            Account loggedInAccount =
                    accountService.loginAccount(account);

            if (loggedInAccount != null) {
                context.status(200).json(loggedInAccount);
            } else {
                context.status(401);
            }

        } catch (Exception e) {
            context.status(401);
        }
    }

    // 3. Create message
    private void createMessageHandler(Context context) {

        try {
            Message message = context.bodyAsClass(Message.class);

            Message createdMessage =
                    messageService.createMessage(message);

            if (createdMessage != null) {
                context.status(200).json(createdMessage);
            } else {
                context.status(400);
            }

        } catch (Exception e) {
            context.status(400);
        }
    }

    // 4. Retrieve all messages
    private void getAllMessagesHandler(Context context) {

        List<Message> messages = messageService.getAllMessages();

        context.status(200).json(messages);
    }

    // 5. Retrieve message by ID
    private void getMessageByIdHandler(Context context) {

        try {
            int messageId =
                    Integer.parseInt(context.pathParam("message_id"));

            Message message = messageService.getMessageById(messageId);

            if (message != null) {
                context.status(200).json(message);
            } else {
                context.status(200);
            }

        } catch (Exception e) {
            context.status(200);
        }
    }

    // 6. Delete message
    private void deleteMessageHandler(Context context) {

        try {
            int messageId =
                    Integer.parseInt(context.pathParam("message_id"));

            Message deletedMessage =
                    messageService.deleteMessage(messageId);

            if (deletedMessage != null) {
                context.status(200).json(deletedMessage);
            } else {
                context.status(200);
            }

        } catch (Exception e) {
            context.status(200);
        }
    }

    // 7. Update message
    private void updateMessageHandler(Context context) {

        try {
            int messageId =
                    Integer.parseInt(context.pathParam("message_id"));

            Message requestMessage =
                    context.bodyAsClass(Message.class);

            Message updatedMessage =
                    messageService.updateMessage(
                            messageId,
                            requestMessage.message_text
                    );

            if (updatedMessage != null) {
                context.status(200).json(updatedMessage);
            } else {
                context.status(400);
            }

        } catch (Exception e) {
            context.status(400);
        }
    }

    // 8. Retrieve messages by account ID
    private void getMessagesByAccountHandler(Context context) {

        try {
            int accountId =
                    Integer.parseInt(context.pathParam("account_id"));

            List<Message> messages =
                    messageService.getMessagesByAccountId(accountId);

            context.status(200).json(messages);

        } catch (Exception e) {
            context.status(200).json(List.of());
        }
    }
}