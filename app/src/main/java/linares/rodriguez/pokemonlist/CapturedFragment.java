package linares.rodriguez.pokemonlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

        // Cargar Pokémon capturados desde Firestore
        //fetchCapturedPokemonFromFirestore();
        loadCapturedPokemon();
        return binding.getRoot();
    }

    private void fetchCapturedPokemonFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("captured").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                capturedPokemonList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Pokemon pokemon = document.toObject(Pokemon.class);
                    capturedPokemonList.add(pokemon);
                }
                System.out.println("Tamaño lista recuperada de firestore: " + capturedPokemonList.size());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Error al cargar Pokémon capturados", Toast.LENGTH_SHORT).show();
            }
            System.out.println("Tamaño lista recuperada de firestore: " + capturedPokemonList.size());
        });
    }

    private void loadCapturedPokemon() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // Obtén los Pokémon capturados desde Firestore
        database.collection("capturados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Vaciar el set de capturados actual para evitar duplicados
                            capturedPokemonList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                System.out.println("Pokemon recuperado de Firestore: " + name);
                                if (name != null) {
                                    // Agregar el Pokémon capturado al set
                                    capturedPokemonList.add(new Pokemon(name));
                                }
                            }
                            // Notificación al usuario
                            Toast.makeText(getContext(), "Se han cargado los Pokémon capturados", Toast.LENGTH_SHORT).show();
                            System.out.println("Tamaño lista recuperada de firestore: " + capturedPokemonList.size());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error cargando lista de Pokémon capturados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}
