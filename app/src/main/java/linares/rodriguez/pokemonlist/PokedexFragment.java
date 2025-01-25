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
        pokedexAdapter = new PokedexAdapter(pokemonManager.getPokemonList(), getContext());
        pokedexAdapter.setOnPokemonCapturedListener(this);
        binding.recyclerViewPokedex.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerViewPokedex.setAdapter(pokedexAdapter);


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.recyclerViewPokedex.setVisibility(View.GONE); // Ocultar RecyclerView temporalmente

        pokemonManager.loadCapturedList(success -> {
            if (success) {
                pokedexAdapter.notifyDataSetChanged();
                binding.recyclerViewPokedex.setVisibility(View.VISIBLE); // Mostrar RecyclerView
            }
        });
    }

    @Override
    public void onPokemonCaptured(Pokemon pokemon) {
        pokemonManager.capturePokemon(pokemon, new PokemonManager.OnCaptureListener() {
            @Override
            public void onSuccess() {
                pokemonManager.loadCapturedList(success -> {
                    if (success) {
                        pokedexAdapter.notifyDataSetChanged(); // Actualizar la vista
                    } else {
                        Toast.makeText(requireContext(), "Error al cargar la lista de capturados", Toast.LENGTH_SHORT).show();
                    }
                });
                System.out.println("Pokemon capturado. Tamaño de lista: " +  pokemonManager.getCapturedList().size());
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(requireContext(), "Error al capturar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}