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
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat {



    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preferences, rootKey);


        SwitchPreferenceCompat languageSwitch = findPreference("language_preference");
        SwitchPreferenceCompat canDeleteSwitch = findPreference("canDelete");
        Preference about = findPreference("about");
        Preference logout = findPreference("logout");



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String savedLanguage = preferences.getString("language", "en"); // "en" es el valor predeterminado

        boolean isEnglish = savedLanguage.equals("en");

        if (languageSwitch != null) {
            languageSwitch.setChecked(isEnglish);
        }

        if (languageSwitch != null) {
            // Agrega un listener para manejar los cambios en el idioma
            languageSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnglishSelected = (boolean) newValue;
                String selectedLanguage = isEnglishSelected ? "en" : "es";
                setLocale(selectedLanguage);
                // Guarda el idioma seleccionado en SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language", selectedLanguage);
                editor.apply();

                 // Actualiza los títulos de las preferencias
                languageSwitch.setTitle(R.string.language);
                about.setTitle(R.string.about);
                canDeleteSwitch.setTitle(R.string.enable_release_pokemon);
                logout.setTitle(R.string.close_session);

                return true;
            });
        }

        about.setOnPreferenceClickListener(preference ->
                showAboutDialog());
        logout.setOnPreferenceClickListener(this::showLogoutConfirmationDialog);
    }

    private boolean showLogoutConfirmationDialog(Preference preference) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.exit_application)
                .setMessage(R.string.sure_to_leave)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    logoutSession(preference);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss(); // Solo cerrar el diálogo
                })
                .show();
        return true;
    }


    private boolean showAboutDialog() {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.about)
                    .setMessage("Roberto Linares Rodríguez\n Versión 1.0.0")
                    .setPositiveButton("OK", null).show();
            return true;
        }


    private void setLocale(String language) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            // Actualiza la configuración de recursos con el nuevo idioma
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            // Aplica los cambios y reinicia la actividad para que el idioma se actualice
            resources.updateConfiguration(config, resources.getDisplayMetrics());

        }


    private boolean logoutSession(Preference preference) {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(preference.getContext(),"Sesion cerrada", Toast.LENGTH_SHORT).show();
                        goToLogin();
                    }
                });
        return true;
    }

    private void goToLogin() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
        //finish();  No funciona, pero hay que finalizar la actividad
    }
}