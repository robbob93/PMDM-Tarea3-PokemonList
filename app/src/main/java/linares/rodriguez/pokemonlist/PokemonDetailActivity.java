package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import linares.rodriguez.pokemonlist.databinding.ActivityPokemonDetailBinding;

public class PokemonDetailActivity extends AppCompatActivity {
    private ActivityPokemonDetailBinding binding;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);
        binding = ActivityPokemonDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();


        System.out.println(intent.getStringExtra("name"));
        System.out.println(intent.getStringExtra("id"));
        System.out.println(intent.getStringExtra("heigth"));
        System.out.println(intent.getStringExtra("weight"));

        binding.pokemonName.setText(intent.getStringExtra("name"));
        binding.pokemonIndex.setText(intent.getStringExtra("id"));
        binding.pokemonHeight.setText(intent.getStringExtra("height"));
        binding.pokemonWeight.setText(intent.getStringExtra("weight"));

        String imageUrl = intent.getStringExtra("imageUrl");
        Picasso.get().load(imageUrl).into(binding.imagePokemon);


    }
}
