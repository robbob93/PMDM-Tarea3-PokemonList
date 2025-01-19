package linares.rodriguez.pokemonlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import linares.rodriguez.pokemonlist.databinding.PokedexFragmentBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokedexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokedexFragmentBinding binding;
    private List<PokedexPokemon> pokedexList;
    private PokedexAdapter adapter;
    private Retrofit retrofit;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokedexFragmentBinding.inflate(inflater, container, false);
        obtenerLista();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Datos de prueba
        pokedexList = new ArrayList<>();
        pokedexList.add(new PokedexPokemon("Bulbasaur"));
        pokedexList.add(new PokedexPokemon("Charmander"));
        pokedexList.add(new PokedexPokemon("Squirtle"));
        pokedexList.add(new PokedexPokemon("Pikachu"));

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_pokedex);
        //PokedexAdapter adapter = new PokedexAdapter(pokedexList);
        binding.recyclerViewPokedex.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewPokedex.setAdapter(adapter);
    }


    public void obtenerLista(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    PokeApiService service = retrofit.create(PokeApiService.class);
    Call<PokeApiResp> pokeApiRespCall = service.getPokemonList(0,150);
    pokeApiRespCall.enqueue(new Callback<PokeApiResp>() {
        @Override
        public void onResponse(Call<PokeApiResp> call, Response<PokeApiResp> response) {
            if(response.isSuccessful() && response.body() != null){
                pokedexList = response.body().getResults();

                adapter = new PokedexAdapter(pokedexList);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onFailure(Call<PokeApiResp> call, Throwable t) {
            Toast.makeText(getContext(), "Fail loading pokedex", Toast.LENGTH_SHORT).show();
            //t.printStackTrace();
        }
    });




    }


}