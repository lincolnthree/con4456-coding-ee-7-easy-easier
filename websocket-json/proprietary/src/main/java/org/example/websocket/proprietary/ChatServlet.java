package org.example.websocket.proprietary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.atmosphere.jboss.as.websockets.WebSocket;
import org.atmosphere.jboss.as.websockets.servlet.WebSocketServlet;
import org.atmosphere.jboss.websockets.Frame;
import org.atmosphere.jboss.websockets.frame.TextFrame;
import org.jboss.logging.Logger;

import org.codehaus.jackson.io.JsonStringEncoder;

@SuppressWarnings("serial")
@WebServlet(ChatServlet.SOCKET_PATH)
public class ChatServlet extends WebSocketServlet
{
   private static final Logger log = Logger.getLogger(ChatServlet.class);

   public static final String SOCKET_PATH = "/socket";
   private static final JsonStringEncoder encoder = JsonStringEncoder.getInstance();

   private Map<String, WebSocket> sockets = new HashMap<String, WebSocket>();

   private List<Pair<Long, String>> informationStorage = new ArrayList<Pair<Long, String>>();

   @Override
   protected void onSocketOpened(WebSocket socket) throws IOException
   {
      log.infof("Websocket opened :) [%s]", socket.getSocketID());
      this.sockets.put(socket.getSocketID(), socket);

      flushInformationTo(this.informationStorage, socket);
   }

   private static void flushInformationTo(List<Pair<Long, String>> informationStorage, WebSocket socket)
   {
      if (!informationStorage.isEmpty())
      {
         for (Pair<Long, String> message : informationStorage)
         {
            writeMessageToSocket(socket, message);
         }
      }
   }

   private static void writeMessageToSockets(Map<String, WebSocket> sockets, Pair<Long, String> message)
   {
      for (Map.Entry<String, WebSocket> entry : sockets.entrySet())
      {
         writeMessageToSocket(entry.getValue(), message);
      }
   }

   private static void writeMessageToSocket(WebSocket socket,
            Pair<Long, String> message)
   {
      try
      {
         socket.writeFrame(TextFrame.from(asJSON(message)
                  ));
      }
      catch (IOException e)
      {
         log.error("IOException writing socket response: " + message, e);
      }
      catch (Error e)
      {
         log.error("Error writing socket response: " + message, e);
      }
   }

   private static String asJSON(Pair<Long, String> message)
   {

      return String.format("{\"timestamp\": \"%s\", \"content\": \"%s\"}",
               encode(message.getLeft().toString()),
               encode(message.getRight())
               );
   }

   private static String encode(String str)
   {
      return new String(encoder.quoteAsUTF8(str));
   }

   @Override
   protected void onSocketClosed(WebSocket socket)
   {
      this.sockets.remove(socket.getSocketID());
      log.infof("Websocket closed :( [%s]", socket.getSocketID());
   }

   @Override
   protected void onReceivedFrame(WebSocket socket)
   {
      try
      {
         final Frame frame = socket.readFrame();
         log.infof("Got a frame [%s] from socket [%s]", frame, socket.getSocketID());
         if (frame instanceof TextFrame)
         {
            final String text = ((TextFrame) frame).getText();
            log.infof("Got message %s", text);
            Pair<Long, String> infoPair = new ImmutablePair<Long, String>(System.currentTimeMillis(), text);
            ChatServlet.this.informationStorage.add(infoPair);
            writeMessageToSockets(ChatServlet.this.sockets, infoPair);
            /*
             * if ("Hello".equals(text)) { socket.writeFrame(TextFrame.from("Hey, there!")); }
             */
         }
      }
      catch (IOException ex)
      {
         log.error("Issue reading frame: " + ex.getMessage());
      }
   }

}
