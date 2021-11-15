package controller;


import models.Categories_response;
import models.Profile;
import models.ProfileNew;

public interface ProfileListener {

    void onSuccess(Profile profile);

    void onSuccess(Categories_response profile);

    void onFailure(String error);
}
