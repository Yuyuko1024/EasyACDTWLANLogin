package org.yuyu.easylogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.button.MaterialButton;
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

    Preference about,opensource_license;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    ATESwitchPreference enable_debug_mode;
    private static final String SHARED_STRING = "LOGIN_INFO";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.main_preference, rootKey);
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_STRING,Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        about=findPreference("about");
        opensource_license=findPreference("opensource_license");
        enable_debug_mode=findPreference("enable_debug_mode");
        if (enable_debug_mode != null) {
            if (!BuildConfig.DEBUG){
                getPreferenceScreen().removePreference(enable_debug_mode);
            }
            enable_debug_mode.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (boolean) newValue;
                sharedEditor.putBoolean("debug_mode",value);
                sharedEditor.commit();
                return true;
            });
        }
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
                notices.addNotice(new Notice("BannerViewPager","https://github.com/zhpanvip/BannerViewPager","Copyright 2017-2020 zhpanvip", new ApacheSoftwareLicense20()));
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
    }
}
