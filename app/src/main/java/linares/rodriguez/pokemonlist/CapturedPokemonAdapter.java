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


/**
 * Adaptador para mostrar la lista de Pokémon capturados en un RecyclerView.
 * Este adaptador gestiona la visualización de los Pokémon capturados y permite
 * la interacción del usuario para liberar o ver los detalles de un Pokémon.
 */
public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.CapturedViewHolder> {
    // Lista de Pokémon capturados que se mostrará en el RecyclerView
    private List<Pokemon> capturedPokemonList;

    // Contexto de la aplicación utilizado para acceder a recursos y vistas
    private final Context context;

    // Escuchadores para manejar eventos de eliminación y clic en el Pokémon
    private OnPokemonRemovedListener removedListener;
    private OnItemClickListener itemClickListener;

    // Bandera que indica si se permite la eliminación de Pokémon
    private boolean canDelete;

    /**
     * Interfaz para escuchar el evento de eliminación de un Pokémon.
     */
    public interface OnPokemonRemovedListener {
        void onPokemonRemoved(Pokemon pokemon);
    }
    /**
     * Establece el escuchador para el evento de eliminación de un Pokémon.
     *
     * @param listener El escuchador que manejará la eliminación del Pokémon.
     */
    public void setOnPokemonRemovedListener(OnPokemonRemovedListener listener) {
        this.removedListener = listener;
    }

    /**
     * Interfaz para escuchar el evento de clic en un Pokémon.
     */
    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

    /**
     * Establece el escuchador para el evento de clic en un Pokémon.
     *
     * @param listener El escuchador que manejará el clic en el Pokémon.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * Constructor del adaptador que inicializa la lista de Pokémon capturados y el contexto.
     *
     * @param capturedPokemonList La lista de Pokémon capturados.
     * @param context El contexto de la aplicación.
     */
    public CapturedPokemonAdapter(List<Pokemon> capturedPokemonList, Context context) {
        this.capturedPokemonList = capturedPokemonList;
        this.context = context;
    }

    /**
     * Crea una nueva vista para un elemento de la lista.
     *
     * @param parent El contenedor donde se agregará la vista.
     * @param viewType El tipo de vista a crear (no utilizado en este caso).
     * @return Un nuevo ViewHolder con la vista inflada.
     */
    @NonNull
    @Override
    public CapturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        CardviewCapturedBinding binding = CardviewCapturedBinding.inflate(inflater, parent, false);
        return new CapturedViewHolder(binding);
    }

    /**
     * Vincula los datos del Pokémon con las vistas del ViewHolder.
     *
     * @param holder El ViewHolder que contiene las vistas.
     * @param position La posición del Pokémon en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull CapturedViewHolder holder, int position) {
        Pokemon pokemon = capturedPokemonList.get(position);  // Obtiene el Pokémon en la posición actual
        holder.bind(pokemon);  // Vincula el Pokémon con las vistas

        List<String> types = pokemon.getTypesNames();  // Obtiene los tipos del Pokémon
        // Construye el nombre del recurso basado en el primer tipo
        String type1 = types.get(0);
        String resourceNameType1 = "label_" + type1.toLowerCase(); // Ejemplo: "label_grass"

        // Obtiene el ID del recurso dinámicamente y lo asigna a la imagen del tipo
        int resourceId = holder.itemView.getContext().getResources().getIdentifier(
                resourceNameType1, "drawable", holder.itemView.getContext().getPackageName());
        holder.binding.imageType.setImageResource(resourceId);

        holder.binding.imageType2.setVisibility(View.INVISIBLE);
        if (pokemon.getTypes().size() > 1) {  // Si el Pokémon tiene dos tipos
            holder.binding.imageType2.setVisibility(View.VISIBLE);// Hace visible la segunda imagen de tipo
            String type2 = types.get(1);  // Obtiene el segundo tipo
            String resourceNameType2 = "label_" + type2.toLowerCase(); // Ejemplo: "label_poison"
            int resourceId2 = holder.itemView.getContext().getResources().getIdentifier(
                    resourceNameType2, "drawable", holder.itemView.getContext().getPackageName());
            holder.binding.imageType2.setImageResource(resourceId2);  // Asigna la imagen del segundo tipo
        }

        // Manejar el clic en la tarjeta para ver el detalle del Pokémon
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(pokemon);  // Llama al listener cuando se hace clic
            }
        });
    }

    /**
     * Devuelve el número total de elementos en la lista de Pokémon.
     *
     * @return El número de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return capturedPokemonList.size();
    }

    /**
     * Establece si se permite o no la eliminación de Pokémon.
     *
     * @param canDelete Valor que indica si se puede eliminar o no.
     */
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    /**
     * ViewHolder que representa un Pokémon en la vista de la lista.
     */
    public class CapturedViewHolder extends RecyclerView.ViewHolder {
        private final CardviewCapturedBinding binding;

        /**
         * Constructor del ViewHolder que vincula las vistas con los elementos UI del Pokémon.
         *
         * @param binding El objeto de enlace de vista (View Binding).
         */
        public CapturedViewHolder(@NonNull CardviewCapturedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Vincula los datos del Pokémon con las vistas del item.
         *
         * @param pokemon El Pokémon que se va a mostrar.
         */
        public void bind(Pokemon pokemon) {
            String nombre = pokemon.getName();
            binding.namePokemon.setText(nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length())); // Pone el nombre en mayúsculas iniciales
            Picasso.get().load(pokemon.getImageUrl()).into(binding.imagePokemon); // Carga la imagen del Pokémon en el ImageView

            binding.releaseButton.setEnabled(canDelete); // Habilita o deshabilita el botón de liberar según "canDelete"
            binding.releaseButton.setOnClickListener(v ->
                    removedListener.onPokemonRemoved(pokemon)); // Notifica al listener cuando se elimina el Pokémon

            // Aplica un filtro de color gris al botón si no se puede eliminar
            if (!canDelete) {
                binding.releaseButton.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN); // Atenúa color
            } else {
                binding.releaseButton.clearColorFilter(); // Restaura color original
            }
        }
    }
}

