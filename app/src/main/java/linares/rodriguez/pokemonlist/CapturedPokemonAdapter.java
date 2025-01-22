package linares.rodriguez.pokemonlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import linares.rodriguez.pokemonlist.databinding.CardviewCapturedBinding;

public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.CapturedViewHolder> {
    private final List<Pokemon> capturedPokemonList;
    private final Context context;
    private OnPokemonRemovedListener removedListener;
    private OnItemClickListener itemClickListener;
    private boolean canDelete;




    public void setOnPokemonRemovedListener(OnPokemonRemovedListener listener) {
        this.removedListener = listener;
    }



    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }




    public CapturedPokemonAdapter(List<Pokemon> capturedPokemonList, Context context) {
        this.capturedPokemonList = capturedPokemonList;
        this.context = context;
        this.canDelete = canDelete;
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



        // Manejar clic en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(pokemon);
            }
        });

    }

    @Override
    public int getItemCount() {
        return capturedPokemonList.size();
    }


    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }







    public class CapturedViewHolder extends RecyclerView.ViewHolder {
        private final CardviewCapturedBinding binding;

        public CapturedViewHolder(@NonNull CardviewCapturedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pokemon pokemon) {
            String nombre = pokemon.getName();
            binding.namePokemon.setText(nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length()));

            Picasso.get().load(pokemon.getImageUrl()).into(binding.imagePokemon);

            binding.releaseButton.setEnabled(canDelete);


            binding.releaseButton.setOnClickListener(v ->
                    removedListener.onPokemonRemoved(pokemon)); // Notifica al fragmento);

            // Aplicar filtro a la imagen si canDelete es false
            if (!canDelete) {
                binding.releaseButton.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN); // Atenuar color
            } else {
                binding.releaseButton.clearColorFilter(); // Restaurar color original
            }


        }
    }
    public interface OnPokemonRemovedListener {
        void onPokemonRemoved(Pokemon pokemon);
    }
}

