package com.udacity.sandwichclub.utils;

import android.util.JsonReader;

import com.udacity.sandwichclub.model.Sandwich;


import org.json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtils {


    public static Sandwich parseSandwichJson(String json){
        Sandwich sandwich = new Sandwich();
        String sandwichName;
        String sandwichPlaceOfOrigin;
        String sandwichDescription;
        String sandwichAKA;
        String sandwichIngredients;
        List<String> sandwichIngredientsList;
        List<String> sandwichIngredientsListFinal = new ArrayList<String>();
        List<String> sandwichAKAs;
        List<String> sandwichAKAsFinal = new ArrayList<String>();
        String sandwichImg;

        try {
            JSONObject jSandwich = new JSONObject(json);
            JSONObject jSandName = jSandwich.getJSONObject("name");

            sandwichName = jSandName.getString("mainName");

            sandwichAKA = jSandName.getString("alsoKnownAs");
            sandwichAKA = sandwichAKA.replace("[", "");
            sandwichAKA = sandwichAKA.replace("]", "");
            sandwichAKAs = new ArrayList<String>(Arrays.asList(sandwichAKA.split("\",")));

            for (String aka : sandwichAKAs) {
                aka = aka.replace("\"", "");
                sandwichAKAsFinal.add(aka);
            }

            sandwichIngredients = jSandwich.getString("ingredients");
            sandwichIngredients = sandwichIngredients.replace("[", "");
            sandwichIngredients = sandwichIngredients.replace("]", "");
            sandwichIngredientsList = new ArrayList<String>(Arrays.asList(sandwichIngredients.split("\",")));

            for (String ingredient : sandwichIngredientsList) {
                ingredient = ingredient.replace("\"", "");
                sandwichIngredientsListFinal.add(ingredient);
            }

            sandwichPlaceOfOrigin = jSandwich.getString("placeOfOrigin");

            sandwichImg = jSandwich.getString("image");
            sandwichDescription = jSandwich.getString("description");



        } catch (JSONException e) {
            return null;
        }

        sandwich.setAlsoKnownAs(sandwichAKAsFinal);
        sandwich.setDescription(sandwichDescription);
        sandwich.setPlaceOfOrigin(sandwichPlaceOfOrigin);
        sandwich.setMainName(sandwichName);
        sandwich.setImage(sandwichImg);
        sandwich.setIngredients(sandwichIngredientsListFinal);

        return sandwich;
    }
}
