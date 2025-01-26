package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import linares.rodriguez.pokemonlist.databinding.ActivityPokemonDetailBinding;


/**
 * Activity que muestra los detalles de un Pokémon, incluyendo su nombre, índice, altura, peso, imagen y tipos.
 */
public class PokemonDetailActivity extends AppCompatActivity {
    private ActivityPokemonDetailBinding binding; // Enlace a las vistas utilizando ViewBinding


    /**
     * Se llama cuando se crea la actividad. Inicializa la vista y configura los elementos.
     *
     * @param savedInstanceState Si la actividad se ha reiniciado, este parámetro contiene el estado previamente guardado.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al método onCreate de la clase base
        setContentView(R.layout.activity_pokemon_detail); // Establece el layout de la actividad
        binding = ActivityPokemonDetailBinding.inflate(getLayoutInflater()); // Infla el binding de la vista
        setContentView(binding.getRoot()); // Establece la raíz del binding como el contenido de la actividad
        binding.backButton.setOnClickListener(view -> onBackPressed()); // Define el comportamiento del botón "back", que vuelve a la actividad anterior


        Intent intent = getIntent();// Obtiene el Intent que inició la actividad
        // Obtiene los extras del intent, como el nombre, tipo(s), ID, altura, peso e imagen del Pokémon
        String nombre = intent.getStringExtra("name");
        ArrayList<String> typeList = intent.getStringArrayListExtra("types");
        binding.pokemonName.setText(nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length())); // Muestra el nombre del Pokémon con la primera letra en mayúscula
        binding.pokemonIndex.setText(intent.getStringExtra("id"));
        binding.pokemonHeight.setText(intent.getStringExtra("height") + "m");
        binding.pokemonWeight.setText(intent.getStringExtra("weight") + "Kg");

        // Carga la imagen del Pokémon desde la URL proporcionada usando Picasso
        String imageUrl = intent.getStringExtra("imageUrl");
        Picasso.get().load(imageUrl).into(binding.imagePokemon);

        String type1 = typeList.get(0);
        String resourceNameType1 = "label_" + type1.toLowerCase(); // Construye el nombre del recurso para el primer tipo. Ejemplo: "label_grass"
        int resourceId = binding.getRoot().getResources().getIdentifier(
                resourceNameType1, "drawable", binding.getRoot().getContext().getPackageName()); // Obtiene el ID del recurso que tenga el identificador del nombre del recurso
        binding.imageType.setImageResource(resourceId);

// Si el Pokémon tiene más de un tipo, maneja el segundo tipo.
        if(typeList.size()>1){
            String type2 = typeList.get(1);
            String resourceNameType2 = "label_" + type2.toLowerCase(); // Ejemplo: "label_fire"
            int resourceId2 = binding.getRoot().getResources().getIdentifier(
                    resourceNameType2, "drawable", binding.getRoot().getContext().getPackageName());
            binding.imageType2.setImageResource(resourceId2);
        }
    }
}
