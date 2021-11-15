package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datauser {
    /* @SerializedName("id")
     @Expose
     private String id;
     @SerializedName("first_name")
     @Expose
     private String firstName;
     @SerializedName("last_name")
     @Expose
     private String lastName;
     @SerializedName("fullname")
     @Expose
     private String fullname;
     @SerializedName("email")
     @Expose
     private String email;
     @SerializedName("profile_picture")
     @Expose
     private Object profilePicture;
     @SerializedName("phone_number")
     @Expose
     private String phoneNumber;
     @SerializedName("role")
     @Expose
     private String role;
     @SerializedName("access_token")
     @Expose
     private String accessToken;
     @SerializedName("token_type")
     @Expose
     private String tokenType;
     @SerializedName("expires_in")
     @Expose
     private Integer expiresIn;

     public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }

     public String getFirstName() {
         return firstName;
     }

     public void setFirstName(String firstName) {
         this.firstName = firstName;
     }

     public String getLastName() {
         return lastName;
     }

     public void setLastName(String lastName) {
         this.lastName = lastName;
     }

     public String getFullname() {
         return fullname;
     }

     public void setFullname(String fullname) {
         this.fullname = fullname;
     }

     public String getEmail() {
         return email;
     }

     public void setEmail(String email) {
         this.email = email;
     }

     public Object getProfilePicture() {
         return profilePicture;
     }

     public void setProfilePicture(Object profilePicture) {
         this.profilePicture = profilePicture;
     }

     public String getPhoneNumber() {
         return phoneNumber;
     }

     public void setPhoneNumber(String phoneNumber) {
         this.phoneNumber = phoneNumber;
     }

     public String getRole() {
         return role;
     }

     public void setRole(String role) {
         this.role = role;
     }

     public String getAccessToken() {
         return accessToken;
     }

     public void setAccessToken(String accessToken) {
         this.accessToken = accessToken;
     }

     public String getTokenType() {
         return tokenType;
     }

     public void setTokenType(String tokenType) {
         this.tokenType = tokenType;
     }

     public Integer getExpiresIn() {
         return expiresIn;
     }

     public void setExpiresIn(Integer expiresIn) {
         this.expiresIn = expiresIn;
     }
 }*/
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
