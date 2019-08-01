package com.example.kitchenrecipes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.kitchenrecipes.adapter.RecipesAdapter;
import com.example.kitchenrecipes.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class RecipeDetail extends AppCompatActivity {


    ImageView img_recipe;
    TextView recipe_ingredient, recipe_step, time_step, name_recipe;

    List<Recipe> recipeList;


    String name, url;
    ScrollView scroll;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        img_recipe = (ImageView) findViewById(R.id.img_recipe);
        recipe_ingredient = (TextView) findViewById(R.id.recipe_ingredient);
        recipe_step = (TextView) findViewById(R.id.recipe_step);
        time_step = (TextView) findViewById(R.id.time_step);
        name_recipe = (TextView) findViewById(R.id.name_recipe);

        scroll=(ScrollView)findViewById(R.id.scrollView2);
        scroll.setBackgroundResource(R.drawable.receta);




        //recogemos los datos del intent

        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("image");


        name_recipe.setTextSize(27);
        time_step.setTextSize(20);
        recipe_step.setTextSize(20);
        recipe_ingredient.setTextSize(20);



        name_recipe.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        name_recipe.setTextColor(Color.parseColor("#FFFFFF"));
        time_step.setTextColor(Color.parseColor("#FFFFFF"));
        recipe_step.setTextColor(Color.parseColor("#FFFFFF"));
        recipe_ingredient.setTextColor(Color.parseColor("#FFFFFF"));


        name_recipe.setText(name);


        Glide.with(getApplicationContext()).load(url).error(R.drawable.comida).into(img_recipe);

        getDataFromApi();




    }

    private void getDataFromApi() {

 String url = "https://ws-prod.eventsnfc.com/sample/recipes.json";

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Recipe>>() {
                    }.getType();
                    recipeList = gson.fromJson(response.toString(), type);


                    //recorro la lista donde estan los datos, una vez encuentro en nombre elegido, paso a los pasos de la receta que se van añadiendo al textview

                   for(int i=0;i<recipeList.size();i++){
                       if(recipeList.get(i).getName().equals(name)){
                           for(int j=0;j<recipeList.get(i).getSteps().size();j++){
                              recipe_step.append("\n"+"\n"+(j+1)+"- "+recipeList.get(i).getSteps().get(j));
                              if(recipeList.get(i).getTimers().get(j)==0||recipeList.get(i).getTimers().get(j)==1) {
                                  time_step.append("\n"+"\n"+(j+1)+"- "+recipeList.get(i).getTimers().get(j)+" min");
                              }else {
                                  time_step.append("\n" + "\n" + (j + 1) + "- " + recipeList.get(i).getTimers().get(j) + " mins");
                              }

                           }
                       }
                   }
                   for(int i=0;i<recipeList.size();i++){
                       if(recipeList.get(i).getName().equals(name)){
                           for(int j=0;j<recipeList.get(i).getIngredients().size();j++){
                               recipe_ingredient.append("\n"+"\n"+"----------------------------------------"+"\n"+"- Name "+ recipeList.get(i).getIngredients().get(j).getName()+"\n"+"\n"+"- Quantity "+recipeList.get(i).getIngredients().get(j).getQuantity()
                                       +"\n"+"\n"+"- Type "+recipeList.get(i).getIngredients().get(j).getType());
                           }
                       }
                   }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RecipeDetail.this, error.getCause() + "", Toast.LENGTH_SHORT).show();

                }
            });
            Volley.newRequestQueue(this).add(request);
        }



}
