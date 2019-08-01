package com.example.kitchenrecipes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.kitchenrecipes.R;
import com.example.kitchenrecipes.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {


    Context context;
    List<Recipe> recipeList;
    List<Recipe>recipeListFiltered;





    OnRecipeClickedListener listener;
    String dificult;



    public RecipesAdapter(Context context, List<Recipe> recipeList, OnRecipeClickedListener listener) {

        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
        this.recipeListFiltered=recipeList;








    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View root= LayoutInflater.from(context).inflate(R.layout.cardview_recipes,parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Recipe recipe = recipeList.get(position);

        //Aqui elijo la dificultad de la receta segun sus pasos

        if (recipe.getSteps().size() <= 4) {
            dificult = "Easy";
        } else if (recipe.getSteps().size() > 4 && recipe.getSteps().size() <= 7) {
            dificult = "Normal";
        } else {
            dificult = "Hard";
        }



        //esto es para saber el estado del boton mediante un entero y cambiar las recetas segun su boto




             holder.dificult.setText("Dificult: " + dificult);



             holder.name.setText(recipe.getName());


             Glide.with(context).load(recipe.getImageURL()).error(R.drawable.comida).into(holder.img);





    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView name;
        TextView dificult;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name_recipe);
            img=(ImageView)itemView.findViewById(R.id.img_recipe);
            dificult=(TextView)itemView.findViewById(R.id.dificult_recipe);



            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            listener.OnRecipeClicked(position);

        }
    }
    public interface OnRecipeClickedListener{
        void OnRecipeClicked(int position);
    }




}












