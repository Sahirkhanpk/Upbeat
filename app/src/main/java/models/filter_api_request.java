package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class filter_api_request {

    @SerializedName("server_key")
    @Expose
    private String  server_key;

    @SerializedName("access_token")
    @Expose
    private String  access_token;

    @SerializedName("user_id")
    @Expose
    private int  user_id;

    public String getServer_key() {
        return server_key;
    }

    public void setServer_key(String server_key) {
        this.server_key = server_key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Integer> attributes) {
        this.attributes = attributes;
    }

    public double getMin_price() {
        return min_price;
    }

    public void setMin_price(double min_price) {
        this.min_price = min_price;
    }

    public double getMax_price() {
        return max_price;
    }

    public void setMax_price(double max_price) {
        this.max_price = max_price;
    }

    @SerializedName("categories")
    @Expose
    private List<Integer> categories;



    @SerializedName("attributes")
    @Expose
    private List<Integer> attributes;





    @SerializedName("min_price")
    @Expose
    private double  min_price;


    @SerializedName("max_price")
    @Expose
    private double  max_price;
}
