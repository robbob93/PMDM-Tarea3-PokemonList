package linares.rodriguez.pokemonlist;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;



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


        if (languageSwitch != null) {
            // Agrega un listener para manejar los cambios en el idioma
            languageSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnglish = (boolean) newValue;
                setLocale(isEnglish ? "en" : "es");

                languageSwitch.setTitle(R.string.language);
                about.setTitle(R.string.about);
                canDeleteSwitch.setTitle(R.string.enable_release_pokemon);
                logout.setTitle(R.string.close_session);
                return true;

            });
        }

        about.setOnPreferenceClickListener(preference ->
                showAboutDialog());

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
        System.out.println("idioma cambiado a " + language);
            Locale.setDefault(locale);
            // Actualiza la configuración de recursos con el nuevo idioma
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            // Aplica los cambios y reinicia la actividad para que el idioma se actualice
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            //getActivity().recreate();

        }
}