package test.myTelegramBot.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("comments")
@Expose
private List<Comment> comments = null;
@SerializedName("total_count")
@Expose
private long totalCount;
@SerializedName("current_user")
@Expose
private Object currentUser;
@SerializedName("show_dfp_ads")
@Expose
private boolean showDfpAds;
@SerializedName("style")
@Expose
private String style;
@SerializedName("isModerator")
@Expose
private boolean isModerator;
@SerializedName("user_id")
@Expose
private long userId;

public List<Comment> getComments() {
return comments;
}

public void setComments(List<Comment> comments) {
this.comments = comments;
}

public long getTotalCount() {
return totalCount;
}

public void setTotalCount(long totalCount) {
this.totalCount = totalCount;
}

public Object getCurrentUser() {
return currentUser;
}

public void setCurrentUser(Object currentUser) {
this.currentUser = currentUser;
}

public boolean isShowDfpAds() {
return showDfpAds;
}

public void setShowDfpAds(boolean showDfpAds) {
this.showDfpAds = showDfpAds;
}

public String getStyle() {
return style;
}

public void setStyle(String style) {
this.style = style;
}

public boolean isIsModerator() {
return isModerator;
}

public void setIsModerator(boolean isModerator) {
this.isModerator = isModerator;
}

public long getUserId() {
return userId;
}

public void setUserId(long userId) {
this.userId = userId;
}

}