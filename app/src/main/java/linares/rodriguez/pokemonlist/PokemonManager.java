package linares.rodriguez.pokemonlist;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonManager {

    private static PokemonManager instance;
    private List<Pokemon> pokemonList; // Lista de Pokémon accesible globalmente
    private final FirebaseFirestore firestore;
    private final String userId;

    private PokemonManager() {
        pokemonList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static synchronized PokemonManager getInstance() {
        if (instance == null) {
            instance = new PokemonManager();
        }
        return instance;
    }

    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public void loadPokemonDataFromApi() {
        // Aquí llamas a la API para cargar los datos iniciales.
        // Añades los Pokémon a `pokemonList`.
    }

    public void capturePokemon(Pokemon pokemon) {
        pokemon.setCaptured(true); // Marcamos el Pokémon como capturado
        saveCapturedPokemonToFirestore(pokemon);
    }

    public void releasePokemon(Pokemon pokemon) {
        pokemon.setCaptured(false); // Marcamos el Pokémon como no capturado
        removeCapturedPokemonFromFirestore(pokemon);
    }

    private void saveCapturedPokemonToFirestore(Pokemon pokemon) {
        firestore.collection("users")
                .document(userId)
                .collection("captured_pokemon")
                .document(String.valueOf(pokemon.getId()))
                .set(pokemon)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Pokemon capturado guardado con éxito."))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar el Pokémon capturado.", e));
    }

    private void removeCapturedPokemonFromFirestore(Pokemon pokemon) {
        firestore.collection("users")
                .document(userId)
                .collection("captured_pokemon")
                .document(String.valueOf(pokemon.getId()))
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Pokemon liberado eliminado con éxito."))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al eliminar el Pokémon liberado.", e));
    }
}
