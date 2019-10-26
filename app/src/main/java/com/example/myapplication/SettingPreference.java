package com.example.myapplication;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

public class SettingPreference extends PreferenceActivity {
	
	ListPreference listPreference;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		listPreference= (ListPreference) findPreference("sort_style");
		listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Snackbar.make(findViewById(android.R.id.content),"重启应用后生效",Snackbar.LENGTH_SHORT).show();
				return true;
			}
		});
		
	}
}
