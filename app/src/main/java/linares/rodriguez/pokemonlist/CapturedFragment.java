package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import linares.rodriguez.pokemonlist.databinding.FragmentCapturedBinding;


/**
 * Fragmento que maneja la visualización y la liberación de los Pokémon capturados.
 * Se encarga de cargar la lista de Pokémon capturados, mostrarla en un RecyclerView
 * y gestionar la interacción del usuario con los Pokémon (detalle y liberación).
 */
public class CapturedFragment extends Fragment {

    // Enlace al objeto de vista de este fragmento, generado a partir del layout XML
    private FragmentCapturedBinding binding;

    // Adaptador para mostrar los Pokémon capturados en el RecyclerView
    private CapturedPokemonAdapter adapter;

    // Lista de Pokémon capturados
    private List<Pokemon> capturedPokemonList = new ArrayList<>();

    // Instancia del manejador de Pokémon para acceder a la lógica de captura/liberación
    private final PokemonManager pokemonManager = PokemonManager.getInstance();



    /**
     * Se ejecuta cuando se crea la vista del fragmento.
     * Configura el RecyclerView, el adaptador y carga los Pokémon capturados.
     *
     * @param inflater El objeto LayoutInflater utilizado para inflar la vista.
     * @param container El contenedor que contiene el fragmento.
     * @param savedInstanceState El estado guardado del fragmento (si existe).
     * @return La vista inflada para este fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCapturedBinding.inflate(inflater, container, false);

        // Configuración del RecyclerView con un LayoutManager para que los elementos se desplieguen en una lista vertical
        binding.capturedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Creación e inicialización del adaptador
        adapter = new CapturedPokemonAdapter(capturedPokemonList, getContext());

        // Asocia el adaptador al RecyclerView
        binding.capturedRecyclerView.setAdapter(adapter);

        // Carga la lista de Pokémon capturados desde el manager
        loadCapturedPokemon();

        // Configura el adaptador con los eventos necesarios (clics, eliminación)
        setupAdapter();

        return binding.getRoot();
    }


    /**
     * Carga la lista de Pokémon capturados desde el PokemonManager
     * y actualiza el RecyclerView con los datos obtenidos.
     */
    private void loadCapturedPokemon() {
        binding.progressBar.setVisibility(View.VISIBLE); // Muestra un círculo de carga mientras se cargan los datos
        pokemonManager.loadCapturedList(success -> {
            if (success) {
                // Si la carga es exitosa, oculta la barra de progreso y actualiza la lista
                binding.progressBar.setVisibility(View.INVISIBLE);
                capturedPokemonList.clear(); // Limpia la lista actual de Pokémon
                capturedPokemonList.addAll(pokemonManager.getCapturedList()); // Añade los Pokémon capturados a la lista
                capturedPokemonList.sort(Comparator.comparingInt(pokemon -> pokemon.getId())); // Ordena los Pokémon por ID
                adapter.notifyDataSetChanged(); // Notifica al adaptador
                if(capturedPokemonList.isEmpty()){
                    Toast.makeText(requireContext(), R.string.pokemonList_empty, Toast.LENGTH_SHORT).show();
                }

            } else {
                // Si ocurre un error al cargar los Pokémon, muestra un mensaje al usuario
                Toast.makeText(requireContext(), R.string.error_loading_captured_pokemon, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Se ejecuta cuando el fragmento está en primer plano, recarga el estado
     * de preferencias, como que el usuario pueda o no eliminar los Pokémon.
     */
    @Override
    public void onResume() {
        super.onResume();
        // Leer el valor actualizado del switch que controla si se pueden eliminar los Pokémon
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean canDelete = preferences.getBoolean("canDelete", false);

        // Actualiza el adaptador con el nuevo valor de "canDelete"
        adapter.setCanDelete(canDelete);
        adapter.notifyDataSetChanged(); // Notifica al adaptador para que adapte la vista
    }



    /**
     * Configura los eventos del adaptador, como el clic en un Pokémon para ver sus detalles y la eliminación de alguno.
     */
    private void setupAdapter() {
        // Configura el evento cuando se hace clic en un Pokémon para mostrar su detalle
        adapter.setOnItemClickListener(pokemon -> {
            ArrayList<String> listaTipos = new ArrayList<>(pokemon.getTypesNames()); // Obtiene los tipos del Pokémon en una lista de cadenas
            System.out.println("Lista tipos: " + listaTipos);

            // Crea una nueva actividad para mostrar los detalles del Pokémon
            Intent intent = new Intent(getContext(), PokemonDetailActivity.class);
            intent.putExtra("name", pokemon.getName());
            intent.putExtra("id", String.valueOf(pokemon.getId()));
            intent.putExtra("types", listaTipos);
            intent.putExtra("height", String.valueOf(pokemon.getHeight()));

            intent.putExtra("weight", String.valueOf(pokemon.getWeight()));
            intent.putExtra("imageUrl", pokemon.getImageUrl());
            startActivity(intent); // Inicia la actividad de detalle del Pokémon
        });

        // Configura el evento cuando se elimina un Pokémon
        adapter.setOnPokemonRemovedListener(pokemon -> {
            pokemonManager.releasePokemon(pokemon, new PokemonManager.OnReleaseListener() {
                @Override
                public void onSuccess(Pokemon pokemon) {
                    String nombre = pokemon.getName().toUpperCase().charAt(0) + pokemon.getName().substring(1, pokemon.getName().length());
                    capturedPokemonList.remove(pokemon); //Se quita el pokemon dado que ha sido satifactorio quitarlo de Firestore

                    Toast.makeText(getContext(), String.format(getString(R.string.free_poke), nombre), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged(); // Notifica al adaptador que la lista ha cambiado
                }

                @Override
                public void onFailure(Exception e) {
                    // Muestra un mensaje de error si no se puede liberar el Pokémon
                    Toast.makeText(getContext(), getString(R.string.error_releasing_pokemon) + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

}
