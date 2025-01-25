package linares.rodriguez.pokemonlist;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonManager {

    private static PokemonManager instance;
    private List<Pokemon> pokemonList; // Lista de Pokémon accesible globalmente



    private List<Pokemon> capturedList;
    private final FirebaseFirestore firestore;
    private final String userId;
    private Retrofit retrofit;

    private PokemonManager() {
        pokemonList = new ArrayList<>();
        capturedList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static synchronized PokemonManager getInstance() {
        if (instance == null) {
            instance = new PokemonManager();
        }
        return instance;
    }

    public List<Pokemon> getCapturedList() {
        return capturedList;
    }

    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    private final List<Runnable> capturedListListeners = new ArrayList<>();

    public void addCapturedListListener(Runnable listener) {
        capturedListListeners.add(listener);
    }

    private void notifyCapturedListUpdated() {
        for (Runnable listener : capturedListListeners) {
            listener.run();
        }
    }


    public void loadPokemonDataFromApi() {
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
                        Pokemon pokemon = new Pokemon(
                                result.getName() // Nombre del Pokémon
                        );
                        pokemonList.add(pokemon);
                    }
                } else {
                    System.out.println("Error al cargar lista de Pokémon");
                    //Toast.makeText(getContext(), "Error al cargar lista de Pokémon", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PokeApiResp> call, Throwable t) {
                System.out.println("Fail loading pokedex");
                //Toast.makeText(getContext(), "Fail loading pokedex", Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void loadCapturedList(OnCaptureLoadCompleteListener listener) {
        firestore.collection("capturados")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    capturedList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String name = document.getString("name");
                        if (name != null && !name.isEmpty()) { // Solo añade Pokémon válidos
                            Pokemon pokemon = document.toObject(Pokemon.class);
                            Log.d("Firestore", "Pokemon cargado: " + pokemon.getName());
                            if (pokemon != null) {
                                capturedList.add(pokemon);
                            }
                        }
                    }
                    listener.onComplete(true);
                    System.out.println("Lista cargada. Tamaño: " + capturedList.size());
                    notifyCapturedListUpdated(); // Notificar cambios
                })
                .addOnFailureListener(e -> {
                    listener.onComplete(false);
                    System.err.println("Error al cargar la lista capturada: " + e.getMessage());
                    notifyCapturedListUpdated(); // Llamar incluso si falla
                });
    }
    // Callback para notificar el estado de carga
    public interface OnCaptureLoadCompleteListener {
        void onComplete(boolean success);
    }




    private void removeCapturedPokemonFromFirestore(Pokemon pokemon) {
        firestore.collection("capturados")
                .document(pokemon.getName())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Pokemon liberado eliminado con éxito."))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al eliminar el Pokémon liberado.", e));
    }

    public interface OnCaptureListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public void capturePokemon(Pokemon pokemon, OnCaptureListener listener) {
        fetchPokemonDetails(pokemon, listener);
    }




    private void fetchPokemonDetails(Pokemon pokemon, OnCaptureListener listener) {
        PokeApiService apiService = retrofit.create(PokeApiService.class);

        String pokemonName = pokemon.getName();
        Call<Pokemon> call = apiService.getPokemonDetails(pokemonName);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon details = response.body();
                    System.out.println("Tipos del pokemon " + details.getName() + " en details: " + details.getTypes());

                    // Actualiza el Pokémon con los detalles adicionales
                    pokemon.setId(details.getId());
                    pokemon.setImageUrl(details.getImageUrl());
                    pokemon.setTypes(details.getTypes());
                    pokemon.setWeight(details.getWeight() / 10f);
                    pokemon.setHeight(details.getHeight() / 10f);

                    // Guardar en Firestore
                    saveCapturedPokemonToFirestore(pokemon, new OnCaptureListener() {
                        @Override
                        public void onSuccess() {
                            capturedList.add(pokemon); // Añadir a la lista de capturados
                            if (listener != null) {
                                listener.onSuccess(); // Notificar al listener del éxito
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            if (listener != null) {
                                listener.onFailure(e); // Notificar al listener del error
                            }
                        }
                    });
                } else {
                    if (listener != null) {
                        listener.onFailure(new Exception("Error al obtener los detalles del Pokémon."));
                    }
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                System.out.println("HA HABIDO UN FALLO EN LA LLAMADA");
                t.printStackTrace();
                if (listener != null) {
                    listener.onFailure(new Exception(t));
                }
            }
        });
    }

    private void saveCapturedPokemonToFirestore(Pokemon pokemon, OnCaptureListener listener) {
        firestore.collection("capturados").document(pokemon.getName())
                .set(pokemon)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Pokemon guardado en Firestore: " + pokemon.getName());
                    if (listener != null) {
                        listener.onSuccess(); // Notificar éxito
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error al guardar en Firestore: " + e.getMessage());
                    if (listener != null) {
                        listener.onFailure(e); // Notificar error
                    }
                });
    }
    public boolean isPokemonCaptured(Pokemon pokemon) {
        for (Pokemon captured : capturedList) {
            if (captured.getName().equals(pokemon.getName())) {
                return true;
            }
        }
        return false;
    }


    private void saveCapturedPokemonToFirestoreConID(Pokemon pokemon) {
        firestore.collection("users")
                .document(userId)
                .collection("captured_pokemon")
                .document(String.valueOf(pokemon.getId()))
                .set(pokemon)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Pokemon capturado guardado con éxito."))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar el Pokémon capturado.", e));
    }
}
