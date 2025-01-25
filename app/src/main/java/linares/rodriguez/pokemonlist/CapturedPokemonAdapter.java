package linares.rodriguez.pokemonlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import linares.rodriguez.pokemonlist.databinding.CardviewCapturedBinding;

public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.CapturedViewHolder> {
    private List<Pokemon> capturedPokemonList;
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
        System.out.println("Tamaño capturedPokemonList: " + capturedPokemonList.size());
        Pokemon pokemon = capturedPokemonList.get(position);
        holder.bind(pokemon);
        System.out.println("Bindeando pokemon " +  pokemon.getName() + " Con tipos: " + pokemon.getTypesNames());
        List<String> types = pokemon.getTypesNames();
        // Construir el nombre del recurso basado en el tipo



        String type1 = types.get(0);
        String resourceNameType1 = "label_" + type1.toLowerCase(); // Ejemplo: "label_grass"
        System.out.println(pokemon.getName()+" Buscando label: " +  resourceNameType1);
        // Obtener el ID del recurso dinámicamente
        int resourceId = holder.itemView.getContext().getResources().getIdentifier(
                resourceNameType1, "drawable", holder.itemView.getContext().getPackageName());
        System.out.println("Resource Id para pokemon " + pokemon.getName()+ ": " + resourceId);



        holder.binding.imageType.setImageResource(resourceId);


        //Hacer invisible porque mantenía imagen de pokemon anteriores
        holder.binding.imageType2.setVisibility(View.INVISIBLE);
        if(pokemon.getTypes().size()>1){
            holder.binding.imageType2.setVisibility(View.VISIBLE);
            String type2 = types.get(1);
            String resourceNameType2 = "label_" + type2.toLowerCase(); // Ejemplo: "label_grass"
            int resourceId2 = holder.itemView.getContext().getResources().getIdentifier(
                    resourceNameType2, "drawable", holder.itemView.getContext().getPackageName());
            holder.binding.imageType2.setImageResource(resourceId2);
            System.out.println(pokemon.getName()+ " Buscando label tipo2: " +  resourceNameType2);
        }




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
            System.out.println("Nombre de pokemon en bind de CapturedPokemonAdapter: " + nombre);
            binding.namePokemon.setText(nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length()));


            System.out.println("Intentando poner imagen de Picasso en el pokemon " + pokemon.getName() + "imageURL: " + pokemon.getImageUrl() + " id: " + pokemon.getId());
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

