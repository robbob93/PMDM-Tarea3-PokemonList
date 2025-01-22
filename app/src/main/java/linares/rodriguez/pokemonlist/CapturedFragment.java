package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import linares.rodriguez.pokemonlist.databinding.FragmentCapturedBinding;

public class CapturedFragment extends Fragment {
    private FragmentCapturedBinding binding;
    private CapturedPokemonAdapter adapter;
    private List<Pokemon> capturedPokemonList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCapturedBinding.inflate(inflater, container, false);

        // Configura RecyclerView
        binding.capturedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CapturedPokemonAdapter(capturedPokemonList, getContext());

        binding.capturedRecyclerView.setAdapter(adapter);
        setupAdapter();
        // Cargar Pokémon capturados desde Firestore
        fetchCapturedPokemonFromFirestore();
        return binding.getRoot();
    }

    private void fetchCapturedPokemonFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("capturados").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                capturedPokemonList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Pokemon pokemon = document.toObject(Pokemon.class);
                    capturedPokemonList.add(pokemon);
                    //System.out.println("Pokemon añadido en la pantalla de capturados: " + pokemon.getName() + " Indice: " + pokemon.getId() + " peso: " +  pokemon.getWeight());
                }
                //Ordenación de la lista
                capturedPokemonList.sort(Comparator.comparingInt(pokemon -> pokemon.getId()));
/*
                System.out.println("Pokemon capturados: ");
                for(Pokemon p : capturedPokemonList){
                    System.out.println(p.getName() + " Id: " + p.getId());
                }

 */
                adapter.notifyDataSetChanged();
                System.out.println("Tamaño lista recuperada de firestore: " + capturedPokemonList.size());
            } else {
                Toast.makeText(getContext(), "Error al cargar Pokémon capturados", Toast.LENGTH_SHORT).show();
            }
            System.out.println("Tamaño lista recuperada de firestore: " + capturedPokemonList.size());
        });

    }


    private void setupAdapter() {
        adapter.setOnItemClickListener(pokemon -> {
            Intent intent = new Intent(getContext(), PokemonDetailActivity.class);
            intent.putExtra("name", pokemon.getName());
            intent.putExtra("id", String.valueOf(pokemon.getId()));
            intent.putExtra("height", String.valueOf(pokemon.getHeight()));
            intent.putExtra("weight", String.valueOf(pokemon.getWeight()));
            intent.putExtra("imageUrl", pokemon.getImageUrl());
            startActivity(intent);
        });


        adapter.setOnPokemonRemovedListener(pokemon -> {

            // Eliminar de Firestore

            System.out.println("Pokemon " + pokemon.getName()+ " pulsado boton liberar");
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("capturados").whereEqualTo("id", pokemon.getId())
                    .get().addOnSuccessListener(querySnapshot ->  {
                        if(querySnapshot.isEmpty()){
                            Toast.makeText(getContext(), "No se pudo eliminar el pokemon", Toast.LENGTH_SHORT).show();
                        }else{
                            querySnapshot.getDocuments().get(0).getReference().delete()
                                    .addOnSuccessListener(runnable ->
                                            System.out.println("Borrado pokemon"))

                                    .addOnFailureListener(runnable ->
                                            System.out.println("No se pudo borrar el pokemon")
                                    );

                            capturedPokemonList.remove(pokemon);
                            adapter.notifyDataSetChanged(); // Actualiza el RecyclerView
                            Toast.makeText(getContext(), String.format("Pokemon %s liberado", pokemon.getName()), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

}
