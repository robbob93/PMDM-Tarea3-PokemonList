package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


/**
 * Actividad para manejar el proceso de inicio de sesión utilizando Firebase Authentication.
 * Permite al usuario iniciar sesión con correo electrónico o Google.
 */
public class LoginActivity extends AppCompatActivity {

    // Instancia de FirebaseAuth para manejar la autenticación de usuarios
    private FirebaseAuth firebaseAuth;

    // Escuchador para detectar cambios en el estado de autenticación
    private FirebaseAuth.AuthStateListener authStateListener;


    /**
     * Método que se llama al crear la actividad.
     * Inicializa FirebaseAuth y configura el escuchador de estado de autenticación.
     *
     * @param savedInstanceState El estado guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa la instancia de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Configura el escuchador de estado de autenticación
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {// Si hay un usuario autenticado, redirige a MainActivity
                goToMainActivity();
            } else {
                startSingIn();// Si no hay un usuario autenticado, inicia el proceso de inicio de sesión
            }
        };
    }

    /**
     * Método que se llama cuando la actividad pasa a primer plano.
     * Registra el escuchador de estado de autenticación.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Agrega el escuchador de cambios en el estado de autenticación
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    /**
     * Método que se llama cuando la actividad pasa a segundo plano.
     * Elimina el escuchador de estado de autenticación para evitar fugas de memoria.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            // Elimina el escuchador de estado de autenticación cuando la actividad ya no está en primer plano
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    /**
     * Inicia el proceso de inicio de sesión utilizando opciones de autenticación como correo electrónico y Google.
     */
    private void startSingIn() {
        // Configura los proveedores de autenticación disponibles (correo electrónico y Google)
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Crea una intent para el proceso de inicio de sesión
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers) // Configura los proveedores disponibles
                .setIsSmartLockEnabled(false) // Desactiva Smart Lock
                .setLogo(R.drawable.logo_masterball) // Establece el logo de la aplicación
                .setTheme(R.style.Theme_PokemonList) // Establece el tema de la actividad
                .build();
        signInLauncher.launch(signInIntent); // Lanza el intent de inicio de sesión
    }

    // Lanzador para manejar el resultado de la actividad de inicio de sesión
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);// Llama al método para manejar el resultado del inicio de sesión
                }
            }
    );

    /**
     * Método que maneja el resultado del inicio de sesión.
     * Si el inicio de sesión es exitoso, redirige a MainActivity.
     * Si hay un error, muestra un mensaje de error.
     *
     * @param result El resultado del intento de inicio de sesión.
     */
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            // Si el inicio de sesión tuvo éxito, se obtiene el usuario y se redirige
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            goToMainActivity();
        } else {
            Toast.makeText(this, "Error login", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método que redirige al usuario a la MainActivity después de un inicio de sesión exitoso.
     * Limpia el stack de actividades para evitar que el usuario regrese a la pantalla de inicio de sesión.
     */
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class); // Crea una intent para ir a MainActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Establece las banderas para limpiar el stack de actividades
        startActivity(intent);
        finish();// Finaliza la actividad de inicio de sesión para evitar que regrese al iniciar sesión
    }
}

