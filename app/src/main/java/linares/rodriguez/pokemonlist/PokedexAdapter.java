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

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {

    private List<Pokemon> pokedexList;
    private Context context;
    private OnPokemonCapturedListener listener;
    PokemonManager pokemonManager = PokemonManager.getInstance();


    public interface OnPokemonCapturedListener {
        void onPokemonCaptured(Pokemon pokemon);
    }

    public void setOnPokemonCapturedListener(OnPokemonCapturedListener listener) {
        this.listener = listener;
    }


    public PokedexAdapter(List<Pokemon> pokedexList, Context context) {
        this.pokedexList = pokedexList;
        this.context = context;
    }


    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewPokedexBinding binding = CardviewPokedexBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PokedexViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon pokemon = pokedexList.get(position);

        boolean isCaptured = pokemonManager.isPokemonCaptured(pokemon);
        holder.bind(pokemon, isCaptured);


        // Cambiar color si está capturado
        if (isCaptured) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.captured_color));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.default_color));
        }

        // Manejar clic en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            if (!isCaptured) {
                pokemonManager.capturePokemon(pokemon, new PokemonManager.OnCaptureListener() {
                    @Override
                    public void onSuccess() {
                        String nombre = pokemon.getName().toUpperCase().charAt(0) + pokemon.getName().substring(1, pokemon.getName().length());
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.captured_color));
                        Toast.makeText(context, String.format(context.getString(R.string.poke_captured), nombre), Toast.LENGTH_SHORT).show();
                        listener.onPokemonCaptured(pokemon); // Refrescar la lista
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, context.getString(R.string.error_capturing) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return pokedexList.size();
    }


    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        private final CardviewPokedexBinding binding;

        public PokedexViewHolder(@NonNull CardviewPokedexBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pokemon pokemon, boolean isCaptured) {
            String nombre = pokemon.getName();
            binding.pkName.setText(nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length()));
            itemView.setBackgroundColor(isCaptured ? Color.parseColor("#DFF2BF") : Color.WHITE);
        }
    }
}
