package linares.rodriguez.pokemonlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import linares.rodriguez.pokemonlist.databinding.PokedexFragmentBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokedexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokedexFragment extends Fragment {

    private PokedexFragmentBinding binding;
    private List<PruebaPokedexPokemon> pokedexList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokedexFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Datos de prueba
        pokedexList = new ArrayList<>();
        pokedexList.add(new PruebaPokedexPokemon("Bulbasaur"));
        pokedexList.add(new PruebaPokedexPokemon("Charmander"));
        pokedexList.add(new PruebaPokedexPokemon("Squirtle"));
        pokedexList.add(new PruebaPokedexPokemon("Pikachu"));

        // Configurar RecyclerView
        PokedexAdapter adapter = new PokedexAdapter(pokedexList);
        binding.recyclerViewPokedex.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewPokedex.setAdapter(adapter);
    }



}