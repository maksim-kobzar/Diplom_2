package org.example;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    private final static String email = RandomStringUtils.randomAlphanumeric(7) + "@" + RandomStringUtils.randomAlphanumeric(4) + ".ry";
    private final static String password = RandomStringUtils.randomAlphanumeric(10);
    private final static String name = RandomStringUtils.randomAlphabetic(5) + RandomStringUtils.randomAlphabetic(4);
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
