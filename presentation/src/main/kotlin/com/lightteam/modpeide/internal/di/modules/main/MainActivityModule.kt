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

package com.lightteam.modpeide.internal.di.modules.main

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import com.lightteam.modpeide.data.delegate.DataLayerDelegate
import com.lightteam.modpeide.data.repository.LocalFileRepository
import com.lightteam.modpeide.data.storage.cache.CacheHandler
import com.lightteam.modpeide.data.storage.database.AppDatabase
import com.lightteam.modpeide.data.storage.keyvalue.PreferenceHandler
import com.lightteam.modpeide.domain.providers.SchedulersProvider
import com.lightteam.modpeide.domain.repository.FileRepository
import com.lightteam.modpeide.internal.di.scopes.PerActivity
import com.lightteam.modpeide.presentation.main.activities.MainActivity
import com.lightteam.modpeide.presentation.main.activities.utils.ToolbarManager
import com.lightteam.modpeide.presentation.main.adapters.DocumentAdapter
import com.lightteam.modpeide.presentation.main.viewmodel.MainViewModel
import com.lightteam.modpeide.presentation.main.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    @PerActivity
    fun provideAppDatabase(context: Context): AppDatabase
            = DataLayerDelegate.provideAppDatabase(context)

    @Provides
    @PerActivity
    fun provideCacheHandler(context: Context): CacheHandler
            = CacheHandler(context)

    @Provides
    @PerActivity
    fun provideFileRepository(database: AppDatabase, sharedPreferences: SharedPreferences): FileRepository
            = LocalFileRepository(database, sharedPreferences)

    @Provides
    @PerActivity
    fun provideMainViewModelFactory(fileRepository: FileRepository,
                                    database: AppDatabase,
                                    schedulersProvider: SchedulersProvider,
                                    preferenceHandler: PreferenceHandler,
                                    cacheHandler: CacheHandler,
                                    documentAdapter: DocumentAdapter): MainViewModelFactory
            = MainViewModelFactory(fileRepository, database, schedulersProvider, preferenceHandler, cacheHandler, documentAdapter)

    @Provides
    @PerActivity
    fun provideMainViewModel(activity: MainActivity, factory: MainViewModelFactory): MainViewModel
            = ViewModelProviders.of(activity, factory).get(MainViewModel::class.java)

    @Provides
    @PerActivity
    fun provideToolbarManager(activity: MainActivity): ToolbarManager
            = ToolbarManager(activity)

    @Provides
    @PerActivity
    fun provideDocumentAdapter(): DocumentAdapter
            = DocumentAdapter()
}