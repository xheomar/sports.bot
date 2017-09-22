package test.myTelegramBot.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

@SerializedName("id")
@Expose
private long id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("img")
@Expose
private String img;
@SerializedName("rating")
@Expose
private long rating;
@SerializedName("stars")
@Expose
private long stars;

public long getId() {
return id;
}

public void setId(long id) {
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

public long getRating() {
return rating;
}

public void setRating(long rating) {
this.rating = rating;
}

public long getStars() {
return stars;
}

public void setStars(long stars) {
this.stars = stars;
}

}