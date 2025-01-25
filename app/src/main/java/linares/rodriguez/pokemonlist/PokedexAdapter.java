package linares.rodriguez.pokemonlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import linares.rodriguez.pokemonlist.databinding.CardviewPokedexBinding;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {

    private List<Pokemon> pokedexList;

    private Context context;
    private OnPokemonCapturedListener listener;
    PokemonManager pokemonManager = PokemonManager.getInstance();
    private final List<OnCapturedListUpdateListener> listeners = new ArrayList<>();

    public interface OnPokemonCapturedListener {
        void onPokemonCaptured(Pokemon pokemon);
    }

    public void setOnPokemonCapturedListener(OnPokemonCapturedListener listener) {
        this.listener = listener;
        System.out.println("Listener configurado: " + (listener != null)); // Agrega esta línea para verificar
    }


    public PokedexAdapter(List<Pokemon> pokedexList, Context context) {
        this.pokedexList = pokedexList;
        this.context = context;
    }

    public void addOnCapturedListUpdateListener(OnCapturedListUpdateListener listener) {
        listeners.add(listener);
    }

    public void notifyCapturedListUpdated() {
        for (OnCapturedListUpdateListener listener : listeners) {
            listener.onCapturedListUpdated();
        }
    }

    public interface OnCapturedListUpdateListener {
        void onCapturedListUpdated();
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
        System.out.println("pokemon " +  pokemon.getName() + "Está capturado? " +  isCaptured);

        holder.bind(pokemon, isCaptured);
        System.out.println("Tamaño de capturados: " +  pokemonManager.getCapturedList().size());




        // Cambiar color si está capturado
        //System.out.println("Tamaño del Set: " +  capturedPokemonSet.size());
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
                        listener.onPokemonCaptured(pokemon); // Refrescar la lista
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "Error al capturar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            binding.pkName.setText(pokemon.getName());
            itemView.setBackgroundColor(isCaptured ? Color.parseColor("#DFF2BF") : Color.WHITE);
        }
    }
}
