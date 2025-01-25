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


/**
 * Actividad principal que gestiona la navegación entre fragmentos de la aplicación,
 * así como la configuración de idioma y carga de datos de Pokémon.
 */
public class MainActivity extends AppCompatActivity {

    // Binding para acceder a las vistas de la actividad
    ActivityMainBinding binding;

    // Controlador de navegación para manejar la navegación entre fragmentos
    NavController navController = null;


    /**
     * Método que se llama cuando la actividad es creada.
     * Configura el idioma de la aplicación y carga los datos de los Pokémon.
     *
     * @param savedInstanceState El estado guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa PokemonManager y carga los datos de Pokémon desde la API
        PokemonManager pokemonManager = PokemonManager.getInstance();
        pokemonManager.loadPokemonDataFromApi();

        // Recupera el idioma guardado en las preferencias de usuario
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String savedLanguage = preferences.getString("language", "en"); // "en" es el valor predeterminado

        // Aplica el idioma guardado
        setLocale(savedLanguage);

        // Infla el layout de la actividad y establece el contenido
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura el controlador de navegación y la vista de navegación inferior
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment != null){
            // Obtiene el controlador de navegación del fragmento
            navController = NavHostFragment.findNavController(navHostFragment);
            // Configura la barra de navegación para trabajar con el controlador de navegación
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
            // Establece como punto de entrada el fragmento de Pokémon capturados
            navController.navigate(R.id.capturedPokemon);
        }
        // Configura el listener para los cambios de selección en el menú de navegación
        binding.bottomNavigationView.setOnItemSelectedListener(this::selectedBottonMenu);


    }

    /**
     * Método que aplica un idioma específico a la aplicación, cambiando la configuración regional.
     *
     * @param language El código de idioma que se desea aplicar.
     */
    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        // Establece la nueva configuración regional en los recursos
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());


    }

    /**
     * Maneja la selección de un ítem en el menú de navegación inferior.
     * Dependiendo del ítem seleccionado, navega al fragmento correspondiente.
     *
     * @param menuItem El ítem de menú seleccionado.
     * @return true si el ítem fue manejado correctamente.
     */
    private boolean selectedBottonMenu(MenuItem menuItem) {
        // Navega al fragmento correspondiente basado en el ítem seleccionado
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