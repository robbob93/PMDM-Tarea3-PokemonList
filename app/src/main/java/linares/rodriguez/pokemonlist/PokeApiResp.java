package linares.rodriguez.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokeApiResp {
    @SerializedName("results")
    private ArrayList<PokemonResult> results;

    public ArrayList<PokemonResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<PokemonResult> results) {
        this.results = results;
    }

    // Modelo interno para representar cada Pokémon en la lista de resultados
    public static class PokemonResult {
        @SerializedName("name")
        private String name;

        @SerializedName("url")
        private String url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
