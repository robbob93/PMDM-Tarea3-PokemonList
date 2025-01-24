package linares.rodriguez.pokemonlist;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import linares.rodriguez.pokemonlist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    NavController navController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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