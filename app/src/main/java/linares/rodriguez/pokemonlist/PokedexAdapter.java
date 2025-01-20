package linares.rodriguez.pokemonlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Set;

import linares.rodriguez.pokemonlist.databinding.CardviewPokedexBinding;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {

    private List<PokedexPokemon> pokedexList;
    private Set<String> capturedPokemonSet;
    private Context context;
    private OnPokemonCapturedListener listener;

    public interface OnPokemonCapturedListener {
        void onPokemonCaptured(String pokemonName);
    }

    public void setOnPokemonCapturedListener(OnPokemonCapturedListener listener) {
        this.listener = listener;
        System.out.println("Listener configurado: " + (listener != null)); // Agrega esta línea para verificar
    }


    public PokedexAdapter(List<PokedexPokemon> pokedexList, Set<String> capturedPokemonSet, Context context) {
        this.pokedexList = pokedexList;
        this.capturedPokemonSet = capturedPokemonSet;
        this.context = context;
    }

    public PokedexAdapter(List<PokedexPokemon> pokedexList, Context context) {
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
        PokedexPokemon pokemon = pokedexList.get(position);
        holder.bind(pokemon, capturedPokemonSet.contains(pokemon.getName()));

        // Cambiar color si está capturado
        System.out.println("Tamaño del Set: " +  capturedPokemonSet.size());
        if (capturedPokemonSet.contains(pokemon.getName())) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.captured_color));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.default_color));
        }

        // Manejar clic en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            if (!capturedPokemonSet.contains(pokemon.getName())) {
                capturedPokemonSet.add(pokemon.getName()); // Añadir a capturados
                if (listener != null) {
                    listener.onPokemonCaptured(pokemon.getName()); // Notificar al fragmento
                    System.out.println("Fragmento notificado");
                }else{
                    System.out.println("Listener es nulo");
                }
                notifyItemChanged(position);
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

        public void bind(PokedexPokemon pokemon, boolean isCaptured) {
            binding.pkName.setText(pokemon.getName());
            itemView.setBackgroundColor(isCaptured ? Color.parseColor("#DFF2BF") : Color.WHITE);
        }
    }

}
