package linares.rodriguez.pokemonlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import linares.rodriguez.pokemonlist.databinding.CardviewPokedexBinding;


/**
 * Adaptador para el RecyclerView que maneja la lista de Pokémon en la Pokédex.
 * Muestra la información de cada Pokémon y permite capturarlos si no han sido capturados previamente.
 */
public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {

    private List<Pokemon> pokedexList; // Lista de Pokémon en la Pokédex
    private Context context; // Contexto de la aplicación
    private OnPokemonCapturedListener listener; // Listener para manejar la captura de un Pokémon
    private PokemonManager pokemonManager = PokemonManager.getInstance(); // Instancia del administrador de Pokémon


    /**
     * Interfaz para manejar cuando un Pokémon es capturado.
     */
    public interface OnPokemonCapturedListener {
        void onPokemonCaptured(Pokemon pokemon);
    }

    /**
     * Establece el listener para manejar la captura de Pokémon.
     *
     * @param listener El listener que manejará la captura del Pokémon.
     */
    public void setOnPokemonCapturedListener(OnPokemonCapturedListener listener) {
        this.listener = listener;
    }


    /**
     * Constructor del adaptador que recibe la lista de Pokémon y el contexto.
     *
     * @param pokedexList La lista de Pokémon a mostrar en la Pokédex.
     * @param context El contexto de la aplicación.
     */
    public PokedexAdapter(List<Pokemon> pokedexList, Context context) {
        this.pokedexList = pokedexList;
        this.context = context;
    }


    /**
     * Crea y devuelve un nuevo ViewHolder para cada elemento del RecyclerView.
     *
     * @param parent El ViewGroup donde se añadirá el nuevo ViewHolder.
     * @param viewType El tipo de vista, no se usa aquí.
     * @return El nuevo ViewHolder para representar un Pokémon.
     */
    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewPokedexBinding binding = CardviewPokedexBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PokedexViewHolder(binding);
    }


    /**
     * Vincula los datos de un Pokémon a un ViewHolder en una posición específica.
     * También maneja la lógica de captura de Pokémon.
     *
     * @param holder El ViewHolder donde se deben mostrar los datos del Pokémon.
     * @param position La posición del Pokémon en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon pokemon = pokedexList.get(position); // Obtiene el Pokémon en la posición
        boolean isCaptured = pokemonManager.isPokemonCaptured(pokemon); // Verifica si el Pokémon ya ha sido capturado
        holder.bind(pokemon, isCaptured);

        // Cambiar el color de fondo según si el Pokémon está capturado o no
        if (isCaptured) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.captured_color));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.default_color));
        }

        // Manejar clic en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            if (!isCaptured) { // Si el Pokémon no ha sido capturado
                pokemonManager.capturePokemon(pokemon, new PokemonManager.OnCaptureListener() {
                    @Override
                    public void onSuccess() { // Si la captura tiene éxito, actualiza la interfaz
                        String nombre = pokemon.getName().toUpperCase().charAt(0) + pokemon.getName().substring(1, pokemon.getName().length());
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.captured_color));
                        Toast.makeText(context, String.format(context.getString(R.string.poke_captured), nombre), Toast.LENGTH_SHORT).show();
                        listener.onPokemonCaptured(pokemon); // Notificar al listener para refrescar la lista
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, context.getString(R.string.error_capturing) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /**
     * Devuelve el número total de elementos en la lista de Pokémon.
     *
     * @return El tamaño de la lista de Pokémon.
     */
    @Override
    public int getItemCount() {
        return pokedexList.size();
    }


    /**
     * ViewHolder que representa cada elemento (Pokémon) en la Pokédex.
     */
    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        private final CardviewPokedexBinding binding;


        /**
         * Constructor del ViewHolder que recibe el binding correspondiente al layout de cada Pokémon.
         *
         * @param binding El binding que contiene las vistas del Pokémon.
         */
        public PokedexViewHolder(@NonNull CardviewPokedexBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        /**
         * Vincula los datos de un Pokémon al ViewHolder.
         *
         * @param pokemon El Pokémon que se debe mostrar.
         * @param isCaptured Indica si el Pokémon ha sido capturado.
         */
        public void bind(Pokemon pokemon, boolean isCaptured) {
            String nombre = pokemon.getName();
            binding.pkName.setText(nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length()));
            itemView.setBackgroundColor(isCaptured ? Color.parseColor("#DFF2BF") : Color.WHITE);
        }
    }
}
