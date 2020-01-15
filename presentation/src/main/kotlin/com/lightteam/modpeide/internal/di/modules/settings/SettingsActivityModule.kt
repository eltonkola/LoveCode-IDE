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

package com.lightteam.modpeide.internal.di.modules.settings

import androidx.lifecycle.ViewModelProviders
import com.lightteam.modpeide.data.storage.keyvalue.PreferenceHandler
import com.lightteam.modpeide.domain.providers.SchedulersProvider
import com.lightteam.modpeide.internal.di.scopes.PerActivity
import com.lightteam.modpeide.presentation.settings.activities.SettingsActivity
import com.lightteam.modpeide.presentation.settings.viewmodel.SettingsViewModel
import com.lightteam.modpeide.presentation.settings.viewmodel.SettingsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SettingsActivityModule {

    @Provides
    @PerActivity
    fun provideSettingsViewModelFactory(schedulersProvider: SchedulersProvider,
                                        preferenceHandler: PreferenceHandler): SettingsViewModelFactory
            = SettingsViewModelFactory(schedulersProvider, preferenceHandler)

    @Provides
    @PerActivity
    fun provideSettingsViewModel(activity: SettingsActivity, factory: SettingsViewModelFactory): SettingsViewModel
            = ViewModelProviders.of(activity, factory).get(SettingsViewModel::class.java)
}