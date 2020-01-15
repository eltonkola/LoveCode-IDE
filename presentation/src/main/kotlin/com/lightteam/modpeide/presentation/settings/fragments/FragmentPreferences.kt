/*
 * Licensed to the Light Team Software (Light Team) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Light Team licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lightteam.modpeide.presentation.settings.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import com.lightteam.modpeide.R
import com.lightteam.modpeide.data.storage.keyvalue.PreferenceHandler
import com.lightteam.modpeide.presentation.base.fragments.DaggerPreferenceFragmentCompat
import com.lightteam.modpeide.presentation.settings.viewmodel.SettingsViewModel
import com.lightteam.modpeide.utils.extensions.asHtml
import com.lightteam.modpeide.utils.extensions.getRawFileText
import javax.inject.Inject

class FragmentPreferences : DaggerPreferenceFragmentCompat() {

    companion object {

        //Headers
        private const val KEY_ROOT = "KEY_HEADER_ROOT"
        private const val KEY_APPLICATION = "KEY_HEADER_APPLICATION"
        private const val KEY_EDITOR = "KEY_HEADER_EDITOR"
        private const val KEY_CODE_STYLE = "KEY_HEADER_CODE_STYLE"
        private const val KEY_FILES = "KEY_HEADER_FILES"
        private const val KEY_ABOUT = "KEY_HEADER_ABOUT"

        //Sensitive
        private const val KEY_THEME = PreferenceHandler.KEY_THEME
        private const val KEY_FONT_TYPE = PreferenceHandler.KEY_FONT_TYPE
        private const val KEY_TAB_LIMIT = PreferenceHandler.KEY_TAB_LIMIT
        private const val KEY_AUTOCLOSE_QUOTES = PreferenceHandler.KEY_AUTOCLOSE_QUOTES
        private const val KEY_ABOUT_AND_CHANGELOG = "ABOUT_AND_CHANGELOG"
        private const val KEY_PRIVACY_POLICY = "PRIVACY_POLICY"
    }

    @Inject
    lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_headers, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when(preference?.key) {
            KEY_ROOT -> setPreferencesFromResource(R.xml.preference_headers, KEY_ROOT)
            KEY_APPLICATION -> setPreferencesFromResource(R.xml.preference_application, KEY_APPLICATION)
            KEY_EDITOR -> setPreferencesFromResource(R.xml.preference_editor, KEY_EDITOR)
            KEY_CODE_STYLE -> setPreferencesFromResource(R.xml.preference_code_style, KEY_CODE_STYLE)
            KEY_FILES -> setPreferencesFromResource(R.xml.preference_files, KEY_FILES)
            KEY_ABOUT -> {
                setPreferencesFromResource(R.xml.preference_about, KEY_ABOUT)

                val changelog = findPreference<Preference>(KEY_ABOUT_AND_CHANGELOG)
                changelog?.setOnPreferenceClickListener { showChangelogDialog() }

                val privacy = findPreference<Preference>(KEY_PRIVACY_POLICY)
                privacy?.setOnPreferenceClickListener { showPrivacyPolicyDialog() }
            }
        }
        activity?.title = preferenceScreen.title
        return super.onPreferenceTreeClick(preference)
    }

    private fun setupObservers() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(preferenceScreen.key != KEY_ROOT) {
                    val root = Preference(context).apply { key = KEY_ROOT }
                    onPreferenceTreeClick(root)
                } else {
                    activity?.finish()
                }
            }
        })
    }

    // region DIALOGS

    private fun showChangelogDialog(): Boolean {
        MaterialDialog(context!!).show {
            title(R.string.dialog_title_changelog)
            message(text = context.getRawFileText(R.raw.changelog).asHtml())
            negativeButton(R.string.action_close)
        }
        return true
    }

    private fun showPrivacyPolicyDialog(): Boolean {
        MaterialDialog(context!!).show {
            title(R.string.dialog_title_privacy_policy)
            message(text = context.getRawFileText(R.raw.privacy_policy).asHtml())
            negativeButton(R.string.action_close)
        }
        return true
    }

    // endregion DIALOGS
}