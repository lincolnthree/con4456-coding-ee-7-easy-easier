package org.example.websocket.proprietary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(ChatServlet.SOCKET_PATH)
public class ChatServlet
{
   private static final Logger log = Logger.getLogger(ChatServlet.class.getName());

   public static final String SOCKET_PATH = "/socket";

   private Map<String, Session> sockets = new HashMap<String, Session>();

   private List<ChatMessage> messageHistory = new ArrayList<ChatMessage>();

   @OnOpen
   public void onSocketOpened(Session socket) throws IOException
   {
      log.log(Level.INFO, "Websocket opened: " + socket.getId());
      sockets.put(socket.getId(), socket);

      for (ChatMessage message : messageHistory)
      {
         socket.getAsyncRemote().sendText(message.toJSON());
      }
   }

   @OnMessage
   public void onMessageReceived(String text)
   {
      log.info("Got message " + text);
      ChatMessage message = new ChatMessage(System.currentTimeMillis(), text);
      messageHistory.add(message);

      for (Map.Entry<String, Session> entry : sockets.entrySet())
      {
         entry.getValue().getAsyncRemote().sendText(message.toJSON());
      }
   }

   @OnClose
   public void onSocketClosed(Session socket)
   {
      sockets.remove(socket.getId());
      log.info("Websocket closed [" + socket.getId() + "]");
   }

}
