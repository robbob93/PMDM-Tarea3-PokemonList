package linares.rodriguez.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Clase que representa la respuesta de la API de Pokémon, que contiene una lista de resultados de Pokémon.
 */
public class PokeApiResp {

    // Lista que contiene los resultados de la API, cada uno representado por un objeto PokemonResult
    @SerializedName("results")
    private ArrayList<PokemonResult> results;

    /**
     * Obtiene la lista de resultados (Pokémon) obtenida desde la API.
     *
     * @return La lista de resultados de la API.
     */
    public ArrayList<PokemonResult> getResults() {
        return results;
    }

    /**
     * Establece la lista de resultados (Pokémon) obtenida desde la API.
     *
     * @param results La lista de resultados de la API.
     */
    public void setResults(ArrayList<PokemonResult> results) {
        this.results = results;
    }

    /**
     * Modelo interno que representa cada Pokémon dentro de la lista de resultados de la API.
     * Cada Pokémon tiene un nombre y una URL de referencia para obtener más información.
     */
    public static class PokemonResult {

        // Nombre del Pokémon, proveniente de la API
        @SerializedName("name")
        private String name;


        /**
         * Obtiene el nombre del Pokémon.
         *
         * @return El nombre del Pokémon.
         */
        public String getName() {
            return name;
        }
    }
}
