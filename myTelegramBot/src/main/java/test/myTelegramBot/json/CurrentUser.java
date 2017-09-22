package test.myTelegramBot.json;

import java.util.HashMap;
import java.util.Map;

public class CurrentUser {

private Integer id;
private String name;
private String img;
private Integer rating;
private Integer stars;
private Boolean isModerator;
private Boolean canComment;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getImg() {
return img;
}

public void setImg(String img) {
this.img = img;
}

public Integer getRating() {
return rating;
}

public void setRating(Integer rating) {
this.rating = rating;
}

public Integer getStars() {
return stars;
}

public void setStars(Integer stars) {
this.stars = stars;
}

public Boolean getIsModerator() {
return isModerator;
}

public void setIsModerator(Boolean isModerator) {
this.isModerator = isModerator;
}

public Boolean getCanComment() {
return canComment;
}

public void setCanComment(Boolean canComment) {
this.canComment = canComment;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}