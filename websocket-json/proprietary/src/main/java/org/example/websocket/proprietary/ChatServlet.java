package org.example.websocket.proprietary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import org.atmosphere.jboss.as.websockets.WebSocket;
import org.atmosphere.jboss.as.websockets.servlet.WebSocketServlet;
import org.atmosphere.jboss.websockets.Frame;
import org.atmosphere.jboss.websockets.frame.TextFrame;

@SuppressWarnings("serial")
@WebServlet(ChatServlet.SOCKET_PATH)
public class ChatServlet extends WebSocketServlet
{
   private static final Logger log = Logger.getLogger(ChatServlet.class.getName());
   public static final String SOCKET_PATH = "/socket";
   private Map<String, WebSocket> sockets = new HashMap<String, WebSocket>();
   private List<ChatMessage> messageHistory = new ArrayList<ChatMessage>();

   @Override
   protected void onSocketOpened(WebSocket socket) throws IOException
   {
      log.info("Socket opened [" + socket.getSocketID() + "]");
      this.sockets.put(socket.getSocketID(), socket);

      writeMessageHistory(socket);
   }

   private void writeMessageHistory(WebSocket socket)
   {
      if (!messageHistory.isEmpty())
      {
         for (ChatMessage message : messageHistory)
         {
            try
            {
               socket.writeFrame(TextFrame.from(message.toJSON()));
            }
            catch (Exception e)
            {
               log.log(Level.SEVERE, "Could not write message: " + message, e);
            }
         }
      }
   }

   @Override
   protected void onSocketClosed(WebSocket socket)
   {
      this.sockets.remove(socket.getSocketID());
      log.info("Socket closed [" + socket.getSocketID() + "]");
   }

   @Override
   protected void onReceivedFrame(WebSocket socket)
   {
      try
      {
         final Frame frame = socket.readFrame();
         log.info("Got a frame [" + frame + "] from socket [" + socket.getSocketID() + "]");
         if (frame instanceof TextFrame)
         {
            final String text = ((TextFrame) frame).getText();
            log.info("Got message: " + text);
            ChatMessage message = new ChatMessage(System.currentTimeMillis(), text);
            ChatServlet.this.messageHistory.add(message);

            for (Map.Entry<String, WebSocket> entry : sockets.entrySet())
            {
               try
               {
                  entry.getValue().writeFrame(TextFrame.from(message.toJSON()));
               }
               catch (Exception e)
               {
                  log.log(Level.SEVERE, "Could not write message: " + message, e);
               }
            }
         }
      }
      catch (IOException ex)
      {
         log.log(Level.SEVERE, "Error reading frame: " + ex.getMessage(), ex);
      }
   }

}
