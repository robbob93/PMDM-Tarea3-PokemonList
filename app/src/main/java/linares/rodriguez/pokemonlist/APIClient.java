package linares.rodriguez.pokemonlist;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * APIClient es una clase que configura y proporciona una instancia de Retrofit
 * para realizar solicitudes HTTP a la API de PokeAPI.
 */
class APIClient {

    // Variable estática para almacenar la instancia de Retrofit
    private static Retrofit retrofit = null;

    /**
     * Devuelve una instancia configurada de Retrofit para interactuar con la API.
     * Este método configura un cliente HTTP con un interceptor para registrar las solicitudes
     * y respuestas, y define la URL base de la API junto con un convertidor Gson
     * para manejar las respuestas en formato JSON.
     *
     * @return Una instancia de Retrofit configurada con la URL base y el convertidor JSON.
     */
    static Retrofit getClient() {
        // Mensaje de depuración en la consola
        System.out.println("OBTENIDO CLIENTE RETROFIT");

        // Configuración de un interceptor para registrar las solicitudes y respuestas HTTP
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Construcción del cliente HTTP con el interceptor configurado
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging) // Agrega el interceptor al cliente HTTP
                .build();

        // Creación de la instancia de Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/") // Define la URL base de la API
                .addConverterFactory(GsonConverterFactory.create()) // Añade el convertidor Gson para procesar JSON automáticamente
                .build();

        // Retorna la instancia de Retrofit configurada
        return retrofit;
    }

}
