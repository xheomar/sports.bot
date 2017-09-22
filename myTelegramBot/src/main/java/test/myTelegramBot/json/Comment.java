package test.myTelegramBot.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

@SerializedName("id")
@Expose
private long id;
@SerializedName("user")
@Expose
private User user;
@SerializedName("c_time")
@Expose
private CTime cTime;
@SerializedName("create_time")
@Expose
private CreateTime createTime;
@SerializedName("text")
@Expose
private String text;
@SerializedName("rating")
@Expose
private Rating rating;
@SerializedName("voteable")
@Expose
private boolean voteable;
@SerializedName("editable")
@Expose
private long editable;
@SerializedName("source_type")
@Expose
private String sourceType;
@SerializedName("message_info")
@Expose
private MessageInfo messageInfo;
@SerializedName("answer_to")
@Expose
private AnswerTo answerTo;

public long getId() {
return id;
}

public void setId(long id) {
this.id = id;
}

public User getUser() {
return user;
}

public void setUser(User user) {
this.user = user;
}

public CTime getCTime() {
return cTime;
}

public void setCTime(CTime cTime) {
this.cTime = cTime;
}

public CreateTime getCreateTime() {
return createTime;
}

public void setCreateTime(CreateTime createTime) {
this.createTime = createTime;
}

public String getText() {
return text;
}

public void setText(String text) {
this.text = text;
}

public Rating getRating() {
return rating;
}

public void setRating(Rating rating) {
this.rating = rating;
}

public boolean isVoteable() {
return voteable;
}

public void setVoteable(boolean voteable) {
this.voteable = voteable;
}

public long getEditable() {
return editable;
}

public void setEditable(long editable) {
this.editable = editable;
}

public String getSourceType() {
return sourceType;
}

public void setSourceType(String sourceType) {
this.sourceType = sourceType;
}

public MessageInfo getMessageInfo() {
return messageInfo;
}

public void setMessageInfo(MessageInfo messageInfo) {
this.messageInfo = messageInfo;
}

public AnswerTo getAnswerTo() {
return answerTo;
}

public void setAnswerTo(AnswerTo answerTo) {
this.answerTo = answerTo;
}

}