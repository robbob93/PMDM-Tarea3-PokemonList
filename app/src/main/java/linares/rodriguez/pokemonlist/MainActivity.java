package linares.rodriguez.pokemonlist;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;

import java.util.ArrayList;
import java.util.List;

import linares.rodriguez.pokemonlist.databinding.ActivityMainBinding;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    NavController navController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PokemonManager pokemonManager = PokemonManager.getInstance();
        pokemonManager.loadPokemonDataFromApi();
        pokemonManager.loadCapturedList(success -> {
            if (success) {
                // Carga exitosa: puedes dejar este bloque vacío o agregar un log
                Log.d("MainActivity", "Lista de Pokémon capturados cargada exitosamente.");




            } else {
                // Manejar error en la carga
                Toast.makeText(this, "Error al cargar la lista de capturados.", Toast.LENGTH_SHORT).show();
            }
        });



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment != null){
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
            //navController.navigate(R.id.capturedPokemon);
            navController.navigate(R.id.capturedPokemon);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(this::selectedBottonMenu);


    }

    private boolean selectedBottonMenu(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.pokedex_menu){
            navController.navigate(R.id.pokedexFragment);
        } else if (menuItem.getItemId() == R.id.settings_menu) {
            navController.navigate(R.id.settingsFragment);
        }else{
            navController.navigate(R.id.capturedPokemon);
        }
        return true;
    }



/*
    private void loadInitialData() {
        PokeApiService apiService = APIClient.getClient().create(PokeApiService.class);

        // Descarga lista completa de Pokémon
        apiService.getPokemonList(0, 100).enqueue(new Callback<PokeApiResp>() {
            @Override
            public void onResponse(Call<PokeApiResp> call, Response<PokeApiResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pokemon> pokemonList = new ArrayList<>();
                    for (PokeApiResp.PokemonResult result : response.body().getResults()) {
                        pokemonList.add(new Pokemon(result.getName()));
                    }
                    PokemonManager.getInstance().setPokemonList(pokemonList);

                    // Después de cargar los Pokémon, carga los capturados
                    loadCapturedPokemon();
                }
            }

            @Override
            public void onFailure(Call<PokeApiResp> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error al cargar los Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCapturedPokemon() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemon")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Pokemon pokemon = doc.toObject(Pokemon.class);
                            PokemonManager.getInstance().addCapturedPokemon(pokemon);
                        }
                    }
                });
    }


 */


}