package linares.rodriguez.pokemonlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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




    public void setOnPokemonRemovedListener(OnPokemonRemovedListener listener) {
        this.removedListener = listener;
    }





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

/*
        if (removedListener != null) {
            System.out.println("Llamada a quitar pokemon");
            removedListener.onPokemonRemoved(pokemon); // Notifica al fragmento
        }

 */
    }

    @Override
    public int getItemCount() {
        return capturedPokemonList.size();
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
            //nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length()).toLowerCase();
            //binding.indexPokemon.setText(String.valueOf(pokemon.getId()));
            //binding.alturaPokemon.setText(String.valueOf(pokemon.getHeight()));
            //binding.pesoPokemon.setText(String.valueOf(pokemon.getWeight()));

            Picasso.get().load(pokemon.getImageUrl()).into(binding.imagePokemon);

            binding.releaseButton.setOnClickListener(v ->
                    removedListener.onPokemonRemoved(pokemon)); // Notifica al fragmento);



/*
            binding.releaseButton.setOnClickListener(view -> {


                System.out.println("Pokemon " + pokemon.getName()+ " pulsado boton liberar");
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection("capturados").whereEqualTo("id", pokemon.getId())
                        .get().addOnSuccessListener(querySnapshot ->  {
                            if(querySnapshot.isEmpty()){
                                Toast.makeText(view.getContext(), "No se pudo eliminar el pokemon", Toast.LENGTH_SHORT).show();
                            }else{
                                querySnapshot.getDocuments().get(0).getReference().delete()
                                        .addOnSuccessListener(runnable ->
                                                System.out.println("Borrado pokemon"))

                                        .addOnFailureListener(runnable ->
                                                System.out.println("No se pudo borrar el pokemon")
                                        );

                            }
                        });

            });

 */
        }
    }
    public interface OnPokemonRemovedListener {
        void onPokemonRemoved(Pokemon pokemon);
    }
}

