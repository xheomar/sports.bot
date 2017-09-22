package test.myTelegramBot.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTime {

@SerializedName("type")
@Expose
private String type;
@SerializedName("timestamp")
@Expose
private long timestamp;
@SerializedName("tolstoy")
@Expose
private String tolstoy;
@SerializedName("bunin")
@Expose
private String bunin;
@SerializedName("bulgakov")
@Expose
private String bulgakov;
@SerializedName("full")
@Expose
private String full;
@SerializedName("short_date")
@Expose
private String shortDate;
@SerializedName("date")
@Expose
private String date;
@SerializedName("lermontov")
@Expose
private String lermontov;
@SerializedName("full_day_of_week")
@Expose
private String fullDayOfWeek;
@SerializedName("short_day_of_week")
@Expose
private String shortDayOfWeek;
@SerializedName("time")
@Expose
private String time;

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public long getTimestamp() {
return timestamp;
}

public void setTimestamp(long timestamp) {
this.timestamp = timestamp;
}

public String getTolstoy() {
return tolstoy;
}

public void setTolstoy(String tolstoy) {
this.tolstoy = tolstoy;
}

public String getBunin() {
return bunin;
}

public void setBunin(String bunin) {
this.bunin = bunin;
}

public String getBulgakov() {
return bulgakov;
}

public void setBulgakov(String bulgakov) {
this.bulgakov = bulgakov;
}

public String getFull() {
return full;
}

public void setFull(String full) {
this.full = full;
}

public String getShortDate() {
return shortDate;
}

public void setShortDate(String shortDate) {
this.shortDate = shortDate;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public String getLermontov() {
return lermontov;
}

public void setLermontov(String lermontov) {
this.lermontov = lermontov;
}

public String getFullDayOfWeek() {
return fullDayOfWeek;
}

public void setFullDayOfWeek(String fullDayOfWeek) {
this.fullDayOfWeek = fullDayOfWeek;
}

public String getShortDayOfWeek() {
return shortDayOfWeek;
}

public void setShortDayOfWeek(String shortDayOfWeek) {
this.shortDayOfWeek = shortDayOfWeek;
}

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

}