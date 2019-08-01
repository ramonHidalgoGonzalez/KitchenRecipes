package com.example.kitchenrecipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenrecipes.adapter.RecipesAdapter;
import com.example.kitchenrecipes.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnRecipeClickedListener   {

    RecyclerView rv_recipes;
    List<Recipe> recipeList;
    List<Recipe>recipeDificult;
    List<Recipe>recipeFilt;
    EditText search_recipe;
    Button btn_dificult;

    int dificult_pos;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_recipe=(EditText) findViewById(R.id.search_recipe);
        btn_dificult=(Button)findViewById(R.id.btn_dificult);

        dificult_pos=0;


        recipeDificult=new ArrayList<>();
        recipeFilt=new ArrayList<>();




        rv_recipes = (RecyclerView) findViewById(R.id.rv_recipes);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rv_recipes.setLayoutManager(manager);

        getDataFromApi();


        search_recipe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {




            }

            @Override
            public void afterTextChanged(Editable s) {
                //Añado a la lista el cambio del texto en tiempo real
                recipeFilt.clear();

                for(int i=0;i<recipeList.size();i++){
                    if(recipeList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        recipeFilt.add(recipeList.get(i));
                    }

                }
                RecipesAdapter adapter = new RecipesAdapter(MainActivity.this, recipeFilt, MainActivity.this);
                rv_recipes.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }


        });

        btn_dificult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeDificult.size()>0) {
                    recipeDificult.clear();
                }


                if(dificult_pos==0){
                    btn_dificult.setText("Easy");
                    for(int i=0;i<recipeList.size();i++){
                        if(recipeList.get(i).getSteps().size() <= 4){
                            recipeDificult.add(recipeList.get(i));
                        }
                    }


                    dificult_pos=1;


                }else if(dificult_pos==1){
                    btn_dificult.setText("Normal");
                    for(int i=0;i<recipeList.size();i++) {
                        if (recipeList.get(i).getSteps().size() > 4 && recipeList.get(i).getSteps().size() <= 7) {
                            recipeDificult.add(recipeList.get(i));
                        }
                    }

                    dificult_pos=2;


                }else if(dificult_pos==2){
                    btn_dificult.setText("Hard");
                    for(int i=0;i<recipeList.size();i++) {
                        if ( recipeList.get(i).getSteps().size() > 7) {
                            recipeDificult.add(recipeList.get(i));
                        }
                    }

                    dificult_pos=3;


                }else{
                    btn_dificult.setText("Random");
                    getDataFromApi();
                    dificult_pos=0;

                }

                RecipesAdapter adapter = new RecipesAdapter(MainActivity.this, recipeDificult, MainActivity.this);
                rv_recipes.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });


    }

    private void getDataFromApi () {
        String url = "https://ws-prod.eventsnfc.com/sample/recipes.json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Recipe>>() {
                }.getType();
                recipeList = gson.fromJson(response.toString(), type);

                RecipesAdapter adapter = new RecipesAdapter(MainActivity.this, recipeList, MainActivity.this);
                rv_recipes.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getCause()+"", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(this).add(request);
    }



    @Override
    public void OnRecipeClicked(int position) {


        Intent i = new Intent(getApplicationContext(), RecipeDetail.class);

        i.putExtra("name",recipeList.get(position).getName());
        i.putExtra("image",recipeList.get(position).getImageURL());


        startActivity(i);

    }

}

