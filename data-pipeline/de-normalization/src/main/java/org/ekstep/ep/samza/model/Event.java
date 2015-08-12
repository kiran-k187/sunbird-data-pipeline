package org.ekstep.ep.samza.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Event {
   private  final Map<String, Object> map;

   public Event(Map<String, Object> map) {
      this.map = map;
   }

   public Child getChild() {
      try {
         String uid = (String) map.get("uid");
         Map<String, Object> udata = (Map<String, Object>) map.get("udata");
         Map<String, Boolean> flags = (Map<String, Boolean>) map.get("flags");
         String timeOfEvent = (String) map.get("ts");
         System.out.println("trying to parse:"+timeOfEvent);
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
         simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
         long timeOfEventTicks = simpleDateFormat.parse(timeOfEvent).getTime();
         System.out.println("successfully parsed");
         Child child = new Child(uid, flags.get("child_data_processed"), timeOfEventTicks);
         child.populate(udata);
         return child;
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return null;
   }

   public void update(Child child) {
      map.put("udata", child.getData());
      Map<String, Boolean> flags = (Map<String, Boolean>) map.get("flags");
      flags.put("child_data_processed", child.isProcessed());
      map.put("flags", flags);
   }

   public Map<String, Object> getData() {
      return map;
   }
}
