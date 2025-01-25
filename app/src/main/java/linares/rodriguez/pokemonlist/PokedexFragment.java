package linares.rodriguez.pokemonlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import linares.rodriguez.pokemonlist.databinding.PokedexFragmentBinding;


/**
 * Fragmento que muestra la Pokédex con una lista de Pokémon.
 * Permite ver y capturar Pokémon. Al capturar un Pokémon, se actualiza la lista de Pokémon capturados.
 */
public class PokedexFragment extends Fragment implements PokedexAdapter.OnPokemonCapturedListener{


    private PokedexFragmentBinding binding; // Binding de las vistas del fragmento
    private PokedexAdapter pokedexAdapter; // Adaptador para el RecyclerView que muestra los Pokémon

    // Instancia de PokemonManager que maneja los Pokémon
    PokemonManager pokemonManager = PokemonManager.getInstance();

    /**
     * Infla el layout del fragmento y configurar el RecyclerView.
     *
     * @param inflater El LayoutInflater para inflar el layout del fragmento.
     * @param container El contenedor donde se debe colocar el layout.
     * @param savedInstanceState El estado guardado del fragmento (si existe).
     * @return La vista inflada del fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokedexFragmentBinding.inflate(inflater, container, false);

        // Configurar RecyclerView
        pokedexAdapter = new PokedexAdapter(pokemonManager.getPokemonList(), getContext());
        pokedexAdapter.setOnPokemonCapturedListener(this);
        // Configurar el layout manager para el RecyclerView
        binding.recyclerViewPokedex.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewPokedex.setAdapter(pokedexAdapter);// Asigna el adaptador al RecyclerView

        return binding.getRoot(); // Devolver la raíz del layout inflado
    }

    /**
     * Método llamado cuando el fragmento ha sido creado.
     *
     * @param view La vista creada del fragmento.
     * @param savedInstanceState El estado guardado del fragmento.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Método llamado cuando el fragmento es reanudado.
     * En este caso, se actualiza la lista de Pokémon capturados por si fueron eliminados
     */
    @Override
    public void onResume() {
        super.onResume();
        binding.recyclerViewPokedex.setVisibility(View.GONE); // Ocultar RecyclerView temporalmente mientras se actualiza la vista

        pokemonManager.loadCapturedList(success -> {
            if (success) {
                pokedexAdapter.notifyDataSetChanged();
                binding.recyclerViewPokedex.setVisibility(View.VISIBLE); // Muestra el RecyclerView actualizado
            }
        });
    }

    /**
     * Método que se llama cuando un Pokémon ha sido capturado.
     * Este método actualiza la lista de Pokémon capturados y refresca la vista.
     *
     * @param pokemon El Pokémon que fue capturado.
     */
    @Override
    public void onPokemonCaptured(Pokemon pokemon) {
        pokemonManager.capturePokemon(pokemon, new PokemonManager.OnCaptureListener() {
            /**
             * Se llama cuando la captura del Pokémon tiene éxito.
             * Se actualiza la lista de Pokémon capturados y la vista del RecyclerView.
             */
            @Override
            public void onSuccess() {
                // Recargar la lista de Pokémon capturados
                pokemonManager.loadCapturedList(success -> {
                    if (success) {
                        pokedexAdapter.notifyDataSetChanged(); // Actualizar la vista
                    } else {
                        Toast.makeText(requireContext(), R.string.error_loading_captured_list, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * Se llama si ocurre un error durante la captura del Pokémon.
             * Muestra un mensaje de error con detalles.
             *
             * @param e La excepción que ocurrió durante la captura.
             */
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(requireContext(), R.string.error_capturing + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}