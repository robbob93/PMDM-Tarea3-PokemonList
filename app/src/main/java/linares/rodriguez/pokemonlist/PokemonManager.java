package linares.rodriguez.pokemonlist;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase responsable de gestionar la lista de Pokémon, su captura, liberación y sincronización con Firestore y la API de PokeAPI.
 */
public class PokemonManager {

    private static PokemonManager instance; // Instancia única de PokemonManager
    private List<Pokemon> pokemonList; // Lista de Pokémon global que se carga desde la API

    private List<Pokemon> capturedList; // Lista de Pokémon capturados
    private final FirebaseFirestore firestore; // Instancia de Firestore para la interacción con la base de datos
    private final String userId; // ID del usuario actual, utilizado para almacenar datos específicos del usuario. Por implementar función
    private Retrofit retrofit; // Instancia de Retrofit para acceder a la API de PokeAPI
    private final List<Runnable> capturedListListeners = new ArrayList<>(); // Lista de oyentes para cambios en la lista de Pokémon capturados

    /**
     * Constructor privado para asegurar que solo haya una instancia de PokemonManager (Singleton).
     */
    private PokemonManager() {
        pokemonList = new ArrayList<>();
        capturedList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * Obtiene la instancia única de PokemonManager a la que acceden el resto de clases de la aplicación.
     *
     * @return La instancia de PokemonManager.
     */
    public static synchronized PokemonManager getInstance() {
        if (instance == null) {
            instance = new PokemonManager();
        }
        return instance;
    }

    /**
     * Obtiene la lista de Pokémon capturados.
     *
     * @return Lista de Pokémon capturados.
     */
    public List<Pokemon> getCapturedList() {
        return capturedList;
    }

    /**
     * Obtiene la lista de Pokémon global.
     *
     * @return Lista de Pokémon.
     */
    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    /**
     * Notifica a los oyentes que la lista de Pokémon capturados ha sido actualizada.
     */
    private void notifyCapturedListUpdated() {
        for (Runnable listener : capturedListListeners) {
            listener.run();
        }
    }

    /**
     * Carga los datos de los Pokémon desde la API de PokeAPI.
     */
    public void loadPokemonDataFromApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/") // Base URL de la API
                .addConverterFactory(GsonConverterFactory.create()) // Utiliza Gson para convertir respuestas JSON
                .build();
        PokeApiService service = retrofit.create(PokeApiService.class); // Crea un servicio de la API de PokeAPI
        Call<PokeApiResp> pokeApiRespCall = service.getPokemonList(0, 150); // Solicita los primeros 150 Pokémon

        pokeApiRespCall.enqueue(new Callback<PokeApiResp>() {
            @Override
            public void onResponse(Call<PokeApiResp> call, Response<PokeApiResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtiene la lista de Pokémon desde la respuesta de la API
                    List<PokeApiResp.PokemonResult> results = response.body().getResults();
                    for (PokeApiResp.PokemonResult result : results) {
                        // Crea un objeto Pokémon con el nombre obtenido
                        Pokemon pokemon = new Pokemon(
                                result.getName() // Nombre del Pokémon
                        );
                        pokemonList.add(pokemon); // Se añade a la lista de Pokémon global
                    }
                } else {
                    System.out.println("Error al cargar lista de Pokémon");
                }
            }
            @Override
            public void onFailure(Call<PokeApiResp> call, Throwable t) {
                System.out.println("Fallo al cargar pokedex");
            }
        });
    }


    /**
     * Carga la lista de Pokémon capturados desde Firestore.
     *
     * @param listener Listener que maneja el resultado de la carga.
     */
    public void loadCapturedList(OnCaptureLoadCompleteListener listener) {
        firestore.collection("capturados")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    capturedList.clear();
                    // Itera sobre los documentos obtenidos y los añade a la lista de Pokémon capturados
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String name = document.getString("name");
                        if (name != null && !name.isEmpty()) { // Solo añade Pokémon válidos, dado que firestore necesita siempre tener un documento en la colección y este puede no ser un pokemon
                            Pokemon pokemon = document.toObject(Pokemon.class);
                            capturedList.add(pokemon);
                        }
                    }
                    listener.onComplete(true); // Notifica que la carga tuvo éxito
                    notifyCapturedListUpdated(); // Notifica cambios
                })
                .addOnFailureListener(e -> {
                    listener.onComplete(false); // Notifica que la carga falló
                    System.err.println("Error al cargar la lista capturada: " + e.getMessage());
                    notifyCapturedListUpdated(); // Notifica incluso si falla
                });
    }

    /**
     * Interface para notificar el estado de la carga de la lista de capturados.
     */
    public interface OnCaptureLoadCompleteListener {
        void onComplete(boolean success);
    }

    /**
     * Libera un Pokémon de la lista de capturados y de Firestore.
     *
     * @param pokemon El Pokémon a liberar.
     * @param listener El listener para manejar el resultado de la liberación.
     */
    public void releasePokemon(Pokemon pokemon, OnReleaseListener listener) {
        // Eliminar de Firestore
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("capturados").whereEqualTo("id", pokemon.getId())
                .get().addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        listener.onFailure(new Exception("No se pudo encontrar el Pokémon en Firestore"));
                    } else {
                        querySnapshot.getDocuments().get(0).getReference().delete()
                                .addOnSuccessListener(runnable -> {

                                    // Elimina de la lista local
                                    capturedList.remove(pokemon);

                                    listener.onSuccess(pokemon); // Notifica que tuvo éxito la liberación
                                })
                                .addOnFailureListener(e -> {
                                    listener.onFailure(new Exception("No se pudo borrar el Pokémon en Firestore"));
                                });
                    }
                });
    }

    /**
     * Interface para escuchar los resultados de la liberación de un Pokémon.
     */
    public interface OnReleaseListener {
        void onSuccess(Pokemon pokemon);
        void onFailure(Exception e);
    }

    /**
     * Interface para escuchar los resultados de la captura de un Pokémon.
     */
    public interface OnCaptureListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    /**
     * Captura un Pokémon y obtiene sus detalles.
     *
     * @param pokemon El Pokémon a capturar.
     * @param listener El listener que maneja el resultado de la captura.
     */
    public void capturePokemon(Pokemon pokemon, OnCaptureListener listener) {
        fetchPokemonDetails(pokemon, listener);
    }



    /**
     * Obtiene los detalles de un Pokémon desde la API de PokeAPI.
     *
     * @param pokemon El Pokémon a capturar.
     * @param listener El listener que maneja el resultado de la captura.
     */
    private void fetchPokemonDetails(Pokemon pokemon, OnCaptureListener listener) {
        PokeApiService apiService = retrofit.create(PokeApiService.class);

        String pokemonName = pokemon.getName();
        Call<Pokemon> call = apiService.getPokemonDetails(pokemonName);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon details = response.body();

                    // Actualiza el Pokémon con los detalles adicionales
                    pokemon.setId(details.getId());
                    pokemon.setImageUrl(details.getImageUrl());
                    pokemon.setTypes(details.getTypes());
                    pokemon.setWeight(details.getWeight() / 10f);
                    pokemon.setHeight(details.getHeight() / 10f);

                    // Guardar el Pokémon en Firestore
                    saveCapturedPokemonToFirestore(pokemon, new OnCaptureListener() {
                        @Override
                        public void onSuccess() {
                            if(!capturedList.contains(pokemon)){
                                capturedList.add(pokemon); // Añadir a la lista de capturados
                                if (listener != null) {
                                    listener.onSuccess(); // Notificar al listener del éxito
                                }
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
                t.printStackTrace();
                if (listener != null) {
                    listener.onFailure(new Exception(t));
                }
            }
        });
    }

    /**
     * Guarda un Pokémon capturado en Firestore.
     *
     * @param pokemon El Pokémon capturado.
     * @param listener El listener que maneja el resultado de la operación.
     */
    private void saveCapturedPokemonToFirestore(Pokemon pokemon, OnCaptureListener listener) {
        firestore.collection("capturados").document(pokemon.getName())
                .set(pokemon)
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onSuccess(); // Notifica que tuvo éxito
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(e); // Notifica que hubo un error
                    }
                });
    }

    /**
     * Comprueba si un Pokémon ya ha sido capturado.
     *
     * @param pokemon El Pokémon a verificar.
     * @return True si el Pokémon ha sido capturado, False de lo contrario.
     */
    public boolean isPokemonCaptured(Pokemon pokemon) {
        for (Pokemon captured : capturedList) {
            if (captured.getName().equals(pokemon.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Guarda un Pokémon capturado en Firestore bajo el ID del usuario. Actualmente por implementar
     *
     * @param pokemon El Pokémon capturado.
     */
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
