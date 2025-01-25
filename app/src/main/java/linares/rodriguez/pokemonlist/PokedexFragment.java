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



public class PokedexFragment extends Fragment implements PokedexAdapter.OnPokemonCapturedListener{


    private PokedexFragmentBinding binding;

    private PokedexAdapter pokedexAdapter;

    PokemonManager pokemonManager = PokemonManager.getInstance();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokedexFragmentBinding.inflate(inflater, container, false);

        // Configurar RecyclerView
        pokedexAdapter = new PokedexAdapter(pokemonManager.getPokemonList(), requireContext());
        pokedexAdapter.setOnPokemonCapturedListener(this);
        binding.recyclerViewPokedex.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerViewPokedex.setAdapter(pokedexAdapter);
        // Esperar a que se carguen los capturados
        pokemonManager.loadCapturedList(success -> {
            if (success) {
                pokedexAdapter.notifyDataSetChanged(); // Actualizar vista tras cargar capturados
            } else {
                Toast.makeText(requireContext(), "Error cargando capturados", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPokemonCaptured(Pokemon pokemon) {
        pokemonManager.capturePokemon(pokemon, new PokemonManager.OnCaptureListener() {
            @Override
            public void onSuccess() {
                //pokemonManager.loadCapturedList();

                //capturedPokemonSet.add(pokemon.getName());
                System.out.println("Pokemon capturado. Tamaño de lista: " +  pokemonManager.getCapturedList().size());
                pokedexAdapter.notifyDataSetChanged(); // Actualizar la vista
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(requireContext(), "Error al capturar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }





}