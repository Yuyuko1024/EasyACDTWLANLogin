package org.yuyu.easylogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import code.name.monkey.appthemehelper.common.prefs.supportv7.ATESwitchPreference;
import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.GnuGeneralPublicLicense30;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense3;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class PreferenceFragment extends PreferenceFragmentCompat {

    Preference about,custom_auth_server,opensource_license;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    ATESwitchPreference enable_custom_auth_server;
    TextInputEditText custom_auth_input;
    private static final String SHARED_STRING = "LOGIN_INFO";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.main_preference, rootKey);
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        about=findPreference("about");
        custom_auth_server=findPreference("auth_server");
        opensource_license=findPreference("opensource_license");
        enable_custom_auth_server=findPreference("enable_custom_auth_server");
        if (about != null) {
            about.setOnPreferenceClickListener(preference -> {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.about)
                        .setMessage(R.string.about_text)
                        .setPositiveButton(R.string.got_it,null)
                        .create()
                        .show();
                return true;
            });
        }
        if(opensource_license != null){
            opensource_license.setOnPreferenceClickListener(preference -> {
                final Notices notices = new Notices();
                notices.addNotice(new Notice("RetroMusic - appthemehelper","https://github.com/RetroMusicPlayer/RetroMusicPlayer","Copyright (c) 2020 Hemanth Savarla.",new GnuGeneralPublicLicense30()));
                notices.addNotice(new Notice("Toasty","https://github.com/GrenderG/Toasty","Copyright 2017 GrenderG",new GnuLesserGeneralPublicLicense3()));
                notices.addNotice(new Notice("okhttp","https://github.com/square/okhttp","Copyright 2019 Square, Inc.",new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Material Components for Android","","Copyright 2017 Google",new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("AndroidX","","Copyright 2005-2011 The Android Open Source Project", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("EasyPermissions","https://github.com/googlesamples/easypermissions","Copyright 2017 Google",new ApacheSoftwareLicense20()));
                new LicensesDialog.Builder(requireContext())
                        .setTitle(R.string.opensource_license)
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();
                return true;
            });
        }
        if(enable_custom_auth_server != null) {
            enable_custom_auth_server.setOnPreferenceChangeListener((preference, newValue) -> {
                final String key = preference.getKey();
                if("enable_custom_auth_server".equals(key)){
                    if(enable_custom_auth_server.isChecked()!=(boolean)newValue) {
                        boolean value = (boolean) newValue;
                        custom_auth_server.setEnabled(value);
                        sharedEditor.putBoolean("enable_custom_auth_server",value);
                        sharedEditor.commit();
                        String summary_text;
                        if(value){
                            summary_text = getString(R.string.auth_server_settings_text, sharedPreferences.getString("auth_server_ip", null));
                        }else{
                            summary_text = getString(R.string.auth_server_settings_text, getString(R.string.auth_server));
                        }
                        custom_auth_server.setSummary(summary_text);
                    }
                }
                return true;
            });
        }
        if(sharedPreferences.getBoolean("enable_custom_auth_server",false)){
            custom_auth_server.setEnabled(true);
            String summary_text = getString(R.string.auth_server_settings_text,sharedPreferences.getString("auth_server_ip",null));
            custom_auth_server.setSummary(summary_text);
        }else{
            custom_auth_server.setEnabled(false);
            String summary_text = getString(R.string.auth_server_settings_text,getString(R.string.auth_server));
            custom_auth_server.setSummary(summary_text);
        }
        custom_auth_server.setOnPreferenceClickListener(preference -> {
            LayoutInflater inflater = getLayoutInflater();
            final View inputLayout = inflater.inflate(R.layout.dialog_custom_auth_srv, null);
            custom_auth_input=inputLayout.findViewById(R.id.custom_auth_input);
            if(sharedPreferences.getString("auth_server_ip",null) == null){
                custom_auth_input.setText(getString(R.string.auth_server));
            }else{
                custom_auth_input.setText(sharedPreferences.getString("auth_server_ip",null));
            }
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.auth_server_settings)
                    .setMessage(R.string.dialog_auth_server_settings_text)
                    .setView(inputLayout)
                    .setCancelable(false)
                    .setPositiveButton(R.string.btn_apply, (dialogInterface, i) -> {
                        if(!Objects.requireNonNull(custom_auth_input.getText()).toString().equals("")){
                            sharedEditor.putString("auth_server_ip",custom_auth_input.getText().toString());
                            sharedEditor.commit();
                            Log.d("Custom IP Address",custom_auth_input.getText().toString());
                            String summary_text = getString(R.string.auth_server_settings_text,custom_auth_input.getText().toString());
                            custom_auth_server.setSummary(summary_text);
                        }else{
                            Snackbar.make(requireView(),R.string.empty_ip_address,Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel,null)
                    .create()
                    .show();
            return true;
        });
    }
}
