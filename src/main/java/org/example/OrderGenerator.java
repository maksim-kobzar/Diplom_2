package org.example;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {

    public List<String> ingredient = new ArrayList<String>();

    public void ingredientHash(String ingredientHash){
        ingredient.add(ingredientHash);
    }

    public Order getHashIngredient(){
        return new Order(ingredient);
    }

}
