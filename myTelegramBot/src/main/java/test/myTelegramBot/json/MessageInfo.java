package test.myTelegramBot.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageInfo {

@SerializedName("id")
@Expose
private long id;
@SerializedName("class")
@Expose
private String _class;
@SerializedName("class_name")
@Expose
private String className;
@SerializedName("class_name_dative")
@Expose
private String classNameDative;
@SerializedName("parent_class_name")
@Expose
private String parentClassName;
@SerializedName("link")
@Expose
private String link;
@SerializedName("name")
@Expose
private String name;
@SerializedName("body")
@Expose
private String body;

public long getId() {
return id;
}

public void setId(long id) {
this.id = id;
}

public String getClass_() {
return _class;
}

public void setClass_(String _class) {
this._class = _class;
}

public String getClassName() {
return className;
}

public void setClassName(String className) {
this.className = className;
}

public String getClassNameDative() {
return classNameDative;
}

public void setClassNameDative(String classNameDative) {
this.classNameDative = classNameDative;
}

public String getParentClassName() {
return parentClassName;
}

public void setParentClassName(String parentClassName) {
this.parentClassName = parentClassName;
}

public String getLink() {
return link;
}

public void setLink(String link) {
this.link = link;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}

}