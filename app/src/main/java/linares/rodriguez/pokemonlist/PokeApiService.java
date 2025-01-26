package linares.rodriguez.pokemonlist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Interfaz que define los servicios de la API de Pokémon utilizando Retrofit.
 * Contiene los métodos para obtener una lista de Pokémon y los detalles de un Pokémon en particular.
 */
public interface PokeApiService {


    /**
     * Obtiene una lista de Pokémon desde la API.
     * Se utiliza para obtener los primeros "limit" Pokémon, comenzando desde el "offset" especificado.
     *
     * @param offset El índice de inicio desde donde se recuperan los Pokémon.
     * @param limit El número máximo de Pokémon a obtener en esta solicitud.
     * @return Un objeto `Call<PokeApiResp>`, que representa la respuesta de la API.
     *         El resultado será un objeto `PokeApiResp` que contiene la lista de Pokémon.
     */
    @GET("pokemon")
    Call<PokeApiResp> getPokemonList(            @Query("offset") int offset,
                                                    @Query("limit") int limit);


    /**
     * Obtiene los detalles de un Pokémon específico desde la API.
     * Se utiliza el nombre del Pokémon para buscar sus detalles completos.
     *
     * @param name El nombre del Pokémon del que se quieren obtener los detalles.
     * @return Un objeto `Call<Pokemon>`, que representa la respuesta de la API.
     *         El resultado será un objeto `Pokemon` que contiene los detalles del Pokémon.
     */
    @GET("pokemon/{name}")
    Call<Pokemon> getPokemonDetails(@Path("name") String name);
}
