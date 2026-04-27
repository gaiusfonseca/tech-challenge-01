package br.gaius.restaurant.controllers;

public class Routes {

    public static final String BASE_URL = "/api/v1";
    public static final String USER_RESOURCE = BASE_URL + "/users";
    public static final String WITH_PAGING = "?page={page}?size={size}";
    public static final String WITH_NAME = "?name={name}" + WITH_PAGING;
    public static final String WITH_ID = "/{id}";
    public static final String PWD_RESOURCE = WITH_ID + "/password";


}
