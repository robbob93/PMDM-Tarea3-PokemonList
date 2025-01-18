package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import linares.rodriguez.pokemonlist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    NavController navController = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment != null){
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
            //NavigationUI.setupActionBarWithNavController(this, navController);
            navController.navigate(R.id.capturedPokemon);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(this::selectedBottonMenu);
        //configureActionBar();
        
        //binding.logoutButton.setOnClickListener(this::logoutSession);
    }

    private boolean selectedBottonMenu(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.pokedex_menu){
            navController.navigate(R.id.pokedexFragment);
        } else if (menuItem.getItemId() == R.id.settings_menu) {
            navController.navigate(R.id.settingsFragment);
        }else{
            navController.navigate(R.id.capturedPokemon);
        }
        return true;
    }

    private void logoutSession(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(view.getContext(),"Sesion cerrada", Toast.LENGTH_SHORT).show();
                        goToLogin();
                    }
                });

    }

    private void goToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}