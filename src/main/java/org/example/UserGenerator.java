package org.example;

public class UserGenerator {

    private static String email = "test22021@ssы58sgdd.ry";
    private static String password = "856456gh4";
    private static String name = "Kolya Baskow";
    public static User getDefault(){
        return new User(email, password, name);
    }

    public static User getNotPasswodr(){
        return new User(email, "", name);
    }

    public static User getAuthorization(){
        return new User(email, password, "");
    }

    public static User getAuthorizationWrongLogin(){
        return new User("email", password, "");
    }

    public static User getAuthorizationWrongPassword(){
        return new User(email, "gfhgfhfgh", "");
    }

    public static User getChangesEmail(){
        return new User("ssd@sss.ss", password, "");
    }

    public static User getChangesName(){
        return new User(email, password, "Ivanыc");
    }

}
