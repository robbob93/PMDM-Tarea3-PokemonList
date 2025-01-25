package linares.rodriguez.pokemonlist;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import java.util.Locale;

import linares.rodriguez.pokemonlist.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    NavController navController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PokemonManager pokemonManager = PokemonManager.getInstance();
        pokemonManager.loadPokemonDataFromApi();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedLanguage = preferences.getString("language", "en"); // "en" es el valor predeterminado

        // Aplicar el idioma guardado
        setLocale(savedLanguage);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment != null){
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
            navController.navigate(R.id.capturedPokemon);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(this::selectedBottonMenu);


    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());


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


}