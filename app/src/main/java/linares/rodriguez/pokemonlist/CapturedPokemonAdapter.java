package linares.rodriguez.pokemonlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import linares.rodriguez.pokemonlist.databinding.CardviewCapturedBinding;

public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.CapturedViewHolder> {
    private final List<Pokemon> capturedPokemonList;
    private final Context context;

    public CapturedPokemonAdapter(List<Pokemon> capturedPokemonList, Context context) {
        this.capturedPokemonList = capturedPokemonList;
        this.context = context;
    }

    @NonNull
    @Override
    public CapturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        CardviewCapturedBinding binding = CardviewCapturedBinding.inflate(inflater, parent, false);
        return new CapturedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CapturedViewHolder holder, int position) {
        Pokemon pokemon = capturedPokemonList.get(position);
        holder.bind(pokemon);

        // Listener para eliminar Pokémon capturado (si es necesario)
        holder.itemView.setOnLongClickListener(v -> {
            // Lógica para eliminar Pokémon de Firestore
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return capturedPokemonList.size();
    }





    public static class CapturedViewHolder extends RecyclerView.ViewHolder {
        private final CardviewCapturedBinding binding;

        public CapturedViewHolder(@NonNull CardviewCapturedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pokemon pokemon) {
            binding.namePokemon.setText(pokemon.getName());
            // Otros datos, como tipo, peso, altura, etc.
        }
    }
}

