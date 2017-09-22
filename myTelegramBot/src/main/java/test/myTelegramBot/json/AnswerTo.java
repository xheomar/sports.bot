package test.myTelegramBot.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerTo {

@SerializedName("user")
@Expose
private User_ user;
@SerializedName("text")
@Expose
private String text;

public User_ getUser() {
return user;
}

public void setUser(User_ user) {
this.user = user;
}

public String getText() {
return text;
}

public void setText(String text) {
this.text = text;
}

}