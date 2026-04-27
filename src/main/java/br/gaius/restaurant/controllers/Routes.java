package br.gaius.restaurant.controllers;

public class Routes {

    public static final String BASE_URL = "/api";
    public static final String VERSION = BASE_URL + "/v1";
    public static final String USER_RESOURCE = VERSION + "/users";
    public static final String WITH_ID = USER_RESOURCE + "/{id}";
    public static final String PWD_RESOURCE = WITH_ID + "/password";


}
