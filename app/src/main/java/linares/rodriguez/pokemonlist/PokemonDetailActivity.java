package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import linares.rodriguez.pokemonlist.databinding.ActivityPokemonDetailBinding;

public class PokemonDetailActivity extends AppCompatActivity {
    private ActivityPokemonDetailBinding binding;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);
        binding = ActivityPokemonDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(view -> onBackPressed());

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("name");
        ArrayList<String> typeList = intent.getStringArrayListExtra("types");
        binding.pokemonName.setText(nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length()));
        //binding.pokemonName.setText(intent.getStringExtra("name"));
        binding.pokemonIndex.setText(intent.getStringExtra("id"));
        binding.pokemonHeight.setText(intent.getStringExtra("height"));
        binding.pokemonWeight.setText(intent.getStringExtra("weight"));

        String imageUrl = intent.getStringExtra("imageUrl");
        Picasso.get().load(imageUrl).into(binding.imagePokemon);

        String type1 = typeList.get(0);
        String resourceNameType1 = "label_" + type1.toLowerCase(); // Ejemplo: "label_grass"
        int resourceId = binding.getRoot().getResources().getIdentifier(
                resourceNameType1, "drawable", binding.getRoot().getContext().getPackageName());
        //System.out.println("Resource Id para pokemon : " + resourceId );
        binding.imageType.setImageResource(resourceId);


        if(typeList.size()>1){
            String type2 = typeList.get(1);
            String resourceNameType2 = "label_" + type2.toLowerCase(); // Ejemplo: "label_grass"
            int resourceId2 = binding.getRoot().getResources().getIdentifier(
                    resourceNameType2, "drawable", binding.getRoot().getContext().getPackageName());
            //System.out.println("Resource Id para pokemon : " + resourceId2 );
            binding.imageType2.setImageResource(resourceId2);

        }

    }
}
