package test.myTelegramBot.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {

@SerializedName("plus")
@Expose
private long plus;
@SerializedName("minus")
@Expose
private long minus;
@SerializedName("current_user_vote")
@Expose
private Object currentUserVote;

public long getPlus() {
return plus;
}

public void setPlus(long plus) {
this.plus = plus;
}

public long getMinus() {
return minus;
}

public void setMinus(long minus) {
this.minus = minus;
}

public Object getCurrentUserVote() {
return currentUserVote;
}

public void setCurrentUserVote(Object currentUserVote) {
this.currentUserVote = currentUserVote;
}

}