package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
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
    private PokemonManager pokemonManager = PokemonManager.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCapturedBinding.inflate(inflater, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean canDelete = preferences.getBoolean("canDelete", false); // false es el valor predeterminado

        // Recuperación de los pokemon almacenados por PokemonManager
        capturedPokemonList = pokemonManager.getCapturedList();
        for(int i=0;i<capturedPokemonList.size();i++){
            System.out.println(capturedPokemonList.get(i).getName() + " con id en capturedFragment " + capturedPokemonList.get(i).getId() +  " y url " + capturedPokemonList.get(i).getImageUrl());
        }


        // Configura RecyclerView
        binding.capturedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CapturedPokemonAdapter(capturedPokemonList, getContext());

        binding.capturedRecyclerView.setAdapter(adapter);
        setupAdapter();

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Leer el valor actualizado del switch
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean canDelete = preferences.getBoolean("canDelete", false);

        // Actualizar el adaptador con el nuevo valor
        adapter.setCanDelete(canDelete);
        adapter.notifyDataSetChanged();
    }




    private void setupAdapter() {
        adapter.setOnItemClickListener(pokemon -> {
            ArrayList<String> listaTipos = new ArrayList<>(pokemon.getTypesNames());
            System.out.println("Lista tipos: " + listaTipos);

            Intent intent = new Intent(getContext(), PokemonDetailActivity.class);
            intent.putExtra("name", pokemon.getName());
            intent.putExtra("id", String.valueOf(pokemon.getId()));
            intent.putExtra("types", listaTipos);
            intent.putExtra("height", String.valueOf(pokemon.getHeight()));

            System.out.println("PESO DE " + pokemon.getName() + " : " +  pokemon.getWeight());
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
