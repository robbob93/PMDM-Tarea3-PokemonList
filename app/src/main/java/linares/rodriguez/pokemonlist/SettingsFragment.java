package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;


/**
 * Fragmento de configuración que maneja las preferencias del usuario, como el idioma, el cierre de sesión y la visualización de información sobre la aplicación.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * Se llama cuando se crean las preferencias del fragmento.
     * Inicializa las preferencias y configura los listeners para manejar cambios en las preferencias.
     *
     * @param savedInstanceState El estado guardado de la instancia (si existe).
     * @param rootKey La clave raíz para las preferencias (si existe).
     */
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        // Asocia las preferencias desde un archivo XML
        setPreferencesFromResource(R.xml.fragment_preferences, rootKey);

        // Obtener preferencias
        SwitchPreferenceCompat languageSwitch = findPreference("language_preference");
        SwitchPreferenceCompat canDeleteSwitch = findPreference("canDelete");
        Preference about = findPreference("about");
        Preference logout = findPreference("logout");

        // Obtener el idioma guardado
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String savedLanguage = preferences.getString("language", "en"); // Valor predeterminado "en"
        boolean isEnglish = savedLanguage.equals("en");

        // Configura el estado del switch de idioma según la preferencia guardada
        if (languageSwitch != null) {
            languageSwitch.setChecked(isEnglish);
            languageSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnglishSelected = (boolean) newValue;
                String selectedLanguage = isEnglishSelected ? "en" : "es";
                setLocale(selectedLanguage);
                // Guarda el idioma seleccionado
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language", selectedLanguage);
                editor.apply();
                // Actualiza los títulos de las preferencias
                updatePreferenceTitles();
                return true;
            });
        }

        // Configura la acción del "About" (Información)
        if (about != null) {
            about.setOnPreferenceClickListener(preference -> showAboutDialog());
        }

        // Configura la acción de "Cerrar sesión"
        if (logout != null) {
            logout.setOnPreferenceClickListener(this::showLogoutConfirmationDialog);
        }

        // Configura la opción de "canDelete" si es necesario
        if (canDeleteSwitch != null) {
            canDeleteSwitch.setOnPreferenceChangeListener((preference, newValue) -> true);
        }
    }

    /**
     * Muestra un cuadro de diálogo de confirmación antes de cerrar sesión.
     *
     * @param preference La preferencia que desencadenó el evento.
     * @return true si el evento fue manejado correctamente.
     */
    private boolean showLogoutConfirmationDialog(Preference preference) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.exit_application)
                .setMessage(R.string.sure_to_leave)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    logoutSession(preference);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Cierra el diálogo
                .show();
        return true;
    }

    /**
     * Muestra un cuadro de diálogo con información sobre la aplicación.
     *
     * @return true si el evento fue manejado correctamente.
     */
    private boolean showAboutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.about)
                .setMessage("Roberto Linares Rodríguez\n Versión 1.0.0")
                .setPositiveButton("OK", null)
                .show();
        return true;
    }


    /**
     * Establece el idioma de la aplicación según el código de idioma proporcionado.
     *
     * @param language El código del idioma (por ejemplo, "en" o "es").
     */
    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    /**
     * Actualiza los títulos de las preferencias en la pantalla de configuración después de cambiar el idioma.
     */
    private void updatePreferenceTitles() {
        Preference languageSwitch = findPreference("language_preference");
        Preference about = findPreference("about");
        Preference canDeleteSwitch = findPreference("canDelete");
        Preference logout = findPreference("logout");

        if (languageSwitch != null) {
            languageSwitch.setTitle(R.string.language);
        }
        if (about != null) {
            about.setTitle(R.string.about);
        }
        if (canDeleteSwitch != null) {
            canDeleteSwitch.setTitle(R.string.enable_release_pokemon);
        }
        if (logout != null) {
            logout.setTitle(R.string.close_session);
        }
    }

    /**
     * Cierra la sesión del usuario actual y redirige a la pantalla de inicio de sesión.
     *
     * @param preference La preferencia que desencadenó el evento.
     * @return true si el evento fue manejado correctamente.
     */
    private boolean logoutSession(Preference preference) {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(task -> {
                    Toast.makeText(preference.getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
                    goToLogin();
                });
        return true;
    }

    /**
     * Redirige al usuario a la pantalla de inicio de sesión.
     */
    private void goToLogin() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        getActivity().finish(); // Finaliza la actividad para evitar regresar a esta pantalla
    }
}
