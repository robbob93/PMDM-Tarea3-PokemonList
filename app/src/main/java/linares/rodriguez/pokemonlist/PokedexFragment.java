package linares.rodriguez.pokemonlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;

import java.util.Set;

import linares.rodriguez.pokemonlist.databinding.PokedexFragmentBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PokedexFragment extends Fragment implements PokedexAdapter.OnPokemonCapturedListener{


    private PokedexFragmentBinding binding;
    private List<PokedexPokemon> pokedexList = new ArrayList<>();
    private Retrofit retrofit;
    private PokedexAdapter pokedexAdapter;


    private Set<String> capturedPokemonSet = new HashSet<>();

    private FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PokedexFragmentBinding.inflate(inflater, container, false);

        loadCapturedPokemon();

        // Configurar RecyclerView
        pokedexAdapter = new PokedexAdapter(pokedexList, capturedPokemonSet, requireContext());
        pokedexAdapter.setOnPokemonCapturedListener(this);
        binding.recyclerViewPokedex.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerViewPokedex.setAdapter(pokedexAdapter);

        System.out.println("Tamaño de pokedexList: " +  pokedexList.size());
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_pokedex);
        //PokedexAdapter adapter = new PokedexAdapter(pokedexList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(PokedexAdapter);

 */
    }

    /*
    Obtiene la lista de pokemon. Solo su nombre y URL
     */
    public List<PokedexPokemon> getPokemonList() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PokeApiService service = retrofit.create(PokeApiService.class);
        Call<PokeApiResp> pokeApiRespCall = service.getPokemonList(0, 150);

        pokeApiRespCall.enqueue(new Callback<PokeApiResp>() {
            @Override
            public void onResponse(Call<PokeApiResp> call, Response<PokeApiResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtiene la lista de Pokémon desde la API
                    List<PokeApiResp.PokemonResult> results = response.body().getResults();
                    for (PokeApiResp.PokemonResult result : results) {
                        // Crea un objeto PokedexPokemon inicial
                        PokedexPokemon pokemon = new PokedexPokemon(
                                result.getName(),  // Nombre del Pokémon
                                0,                 // ID aún no disponible
                                "",                // URL de imagen aún no disponible
                                new ArrayList<>(), // Tipos aún no disponibles
                                0,                 // Peso aún no disponible
                                0                  // Altura aún no disponible
                        );

                        // Añádelo a la lista y llama a fetchPokemonDetails para completar los detalles
                        pokedexList.add(pokemon);
                        fetchPokemonDetails(result.getUrl(), pokemon);
                        //System.out.println("Pokemon: " + result.getName() + " tiene la URL: " + result.getUrl());
                    }

                    // Inicializa el adaptador aquí, asegurándose de que ya se pase el Set de Pokémon capturados
                    PokedexAdapter pokedexAdapter = new PokedexAdapter(pokedexList, capturedPokemonSet, getContext());

                    // Configura el listener para manejar la captura de Pokémon
                    pokedexAdapter.setOnPokemonCapturedListener(new PokedexAdapter.OnPokemonCapturedListener() {
                        @Override
                        public void onPokemonCaptured(PokedexPokemon pokemon) {
                            // Agrega el Pokémon al conjunto de capturados
                            capturedPokemonSet.add(pokemon.getName());
                            System.out.println("Se ha capturado el pokemon: " + pokemon.getName());
                            // Si necesitas actualizar algo en la vista, puedes hacerlo aquí
                            saveCapturedPokemonToFirestore(pokemon);
                            pokedexAdapter.notifyDataSetChanged();
                        }
                    });

                    // Establecer el adaptador al RecyclerView
                    binding.recyclerViewPokedex.setAdapter(pokedexAdapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar lista de Pokémon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokeApiResp> call, Throwable t) {
                Toast.makeText(getContext(), "Fail loading pokedex", Toast.LENGTH_SHORT).show();
            }
        });
        return pokedexList;
    }

    private void fetchPokemonDetails(String url, PokedexPokemon pokemon) {
        PokeApiService apiService = retrofit.create(PokeApiService.class);

        // Extrae el nombre del Pokémon desde la URL
        String pokemonName = url.substring(url.lastIndexOf("/", url.length() - 2) + 1, url.length() - 1);

        Call<PokemonDetails> call = apiService.getPokemonDetails(pokemonName);
        call.enqueue(new Callback<PokemonDetails>() {
            @Override
            public void onResponse(Call<PokemonDetails> call, Response<PokemonDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetails details = response.body();

                    // Actualiza el Pokémon con los detalles adicionales
                    pokemon.setId(details.getId());
                    pokemon.setImageUrl(details.getImageUrl());
                    //pokemon.setTypes(details.getTypeNames());
                    pokemon.setWeight(details.getWeight());
                    pokemon.setHeight(details.getHeight());

                    // Notifica al adaptador para que actualice la vista
                    pokedexAdapter.notifyDataSetChanged();
                }

                //System.out.println("Pokemon " +  pokemon.getName() + "pesa: " + pokemon.getWeight());
            }

            @Override
            public void onFailure(Call<PokemonDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void saveCapturedPokemonToFirestore(PokedexPokemon pokemon) {
        database.collection("capturados")
                .add(pokemon).addOnSuccessListener(runnable ->
                        Toast.makeText(getContext(),"Saved Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(runnable ->
                        Toast.makeText(getContext(),"Saved Failure", Toast.LENGTH_SHORT).show());
    }


    private void loadCapturedPokemon() {

        // Obtén los Pokémon capturados desde Firestore
        database.collection("capturados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Vaciar el set de capturados actual para evitar duplicados
                            capturedPokemonSet.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                if (name != null) {
                                    // Agregar el Pokémon capturado al set
                                    capturedPokemonSet.add(name);
                                }
                            }
                            // Notificación al usuario
                            Toast.makeText(getContext(), "Se han cargado los Pokémon capturados", Toast.LENGTH_SHORT).show();

                            getPokemonList(); //Necesario que esté aqui para que cargue automaticamente los capturados
                            System.out.println("Tamaño de pokedexList: " +  pokedexList.size());

                            // Notifica al adaptador que los datos han cambiado
                            System.out.println("Tamaño del Set Desde Firestore: " + capturedPokemonSet.size());
                            pokedexAdapter.notifyDataSetChanged();  // Notificar al adaptador que los datos de capturados han cambiado
                        } else {
                            Toast.makeText(getContext(), "Error cargando lista de Pokémon capturados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onPokemonCaptured(PokedexPokemon pokemon) {

    }



/*
    Sin Uso

    private void updateCapturedPokemonInFirestore(String pokemonName) {
        // Crear un documento con el nombre del Pokémon como campo
        Map<String, Object> data = new HashMap<>();
        data.put("name", pokemonName);
        System.out.println("Dentro de updateCapturedInFirestore");
        // Añadir a la colección "capturados"
        database.collection("capturados")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Pokemon capturado actualizado en Firestore: " + pokemonName);
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error actualizando Firestore para " + pokemonName + ": " + e.getMessage());
                });
    }


        private void saveCapturedPokemonToFirebase(PokemonData pokemon) {
        // Crear un mapa para guardar los datos en Firestore
        Map<String, Object> pokemonMap = new HashMap<>();
        pokemonMap.put("name", pokemon.getName());
        pokemonMap.put("number", pokemon.getIndex());
        pokemonMap.put("url", pokemon.getImageURL());
        pokemonMap.put("types", pokemon.getTipo());
        pokemonMap.put("weight", pokemon.getPeso());
        pokemonMap.put("height", pokemon.getAltura());
        pokemonMap.put("capturado", true); // Marcar como capturado

        database.collection("capturados")
                .document(pokemon.getName().toLowerCase())
                .set(pokemonMap) // Usar el mapa en lugar del objeto Pokemon
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "captured" + " " + pokemon.getName(), Toast.LENGTH_SHORT).show();
                    // Actualizar el estado del Pokémon en la lista
                    capturedPokemonSet.add(pokemon.getName().toLowerCase());
                    //listaPokedexAdapter.setCapturadosMap(capturadosMap);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


 */


}