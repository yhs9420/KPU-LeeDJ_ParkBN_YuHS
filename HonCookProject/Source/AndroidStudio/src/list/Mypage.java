package org.tensorflow.demo.list;

public class Mypage {
    String userID;
    String userAge;
    int recipe_id;
    String recipe_name;
    String recipe_category;
    String recipe_ingredient;
    String recipe_content;

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_category() {
        return recipe_category;
    }

    public void setRecipe_category(String recipe_category) {
        this.recipe_category = recipe_category;
    }

    public String getRecipe_ingredient() {
        return recipe_ingredient;
    }

    public void setRecipe_ingredient(String recipe_ingredient) {
        this.recipe_ingredient = recipe_ingredient;
    }

    public String getRecipe_content() {
        return recipe_content;
    }

    public void setRecipe_content(String recipe_content) {
        this.recipe_content = recipe_content;
    }

    public Mypage(String userID, String userAge, int recipe_id, String recipe_name, String recipe_category, String recipe_ingredient, String recipe_content) {
        this.userID = userID;
        this.userAge = userAge;
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.recipe_category = recipe_category;
        this.recipe_ingredient = recipe_ingredient;
        this.recipe_content = recipe_content;
    }
}