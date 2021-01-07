package com.DataBaseRestApi.DataBaseRestApi;

public class Controller {
    DataBaseConnection dbCon;

    public Controller() {
        dbCon = DataBaseConnection.getInstance();


        try {


        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        dbCon.close();


    }
}
