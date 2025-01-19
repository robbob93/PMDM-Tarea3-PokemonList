package linares.rodriguez.pokemonlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import linares.rodriguez.pokemonlist.databinding.CardviewPokedexBinding;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {

    private List<PokedexPokemon> pokedexList;

    public PokedexAdapter(List<PokedexPokemon> pokedexList) {
        this.pokedexList = pokedexList;
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
        PokedexPokemon pokemon = pokedexList.get(position);
        holder.bind(pokemon);
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

        public void bind(PokedexPokemon pokemon) {
            binding.pkName.setText(pokemon.getName());
        }
    }
}
