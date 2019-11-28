package main;

import auth.AuthActiveDirectory;

public class MainClass {

    public static void main(String[] args) {
        try {
            AuthActiveDirectory.getConnection("Togrul", "togrul123");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
