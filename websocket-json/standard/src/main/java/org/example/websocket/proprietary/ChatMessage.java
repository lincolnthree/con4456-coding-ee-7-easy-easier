/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.example.websocket.proprietary;

import javax.json.Json;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ChatMessage
{
   private long time;
   private String text;

   public ChatMessage(long time, String text)
   {
      this.time = time;
      this.text = text;
   }

   public String getText()
   {
      return text;
   }

   public long getTime()
   {
      return time;
   }

   public String toJSON()
   {
      return Json.createObjectBuilder()
               .add("timestamp", time)
               .add("content", text).build().toString();
   }

}
