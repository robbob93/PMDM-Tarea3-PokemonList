package linares.rodriguez.pokemonlist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeApiService {
    @GET("pokemon")
    Call<PokeApiResp> getPokemonList(            @Query("offset") int offset,
                                                    @Query("limit") int limit);
    @GET("pokemon/{name}")
    Call<Pokemon> getPokemonDetails(@Path("name") String name);
}
