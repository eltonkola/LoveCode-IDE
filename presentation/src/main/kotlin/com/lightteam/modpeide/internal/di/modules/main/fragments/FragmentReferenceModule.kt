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

package com.lightteam.modpeide.internal.di.modules.main.fragments

import com.lightteam.modpeide.internal.di.scopes.PerFragment
import com.lightteam.modpeide.presentation.main.adapters.FileAdapter
import com.lightteam.modpeide.presentation.main.adapters.interfaces.RecyclerSelection
import com.lightteam.modpeide.presentation.main.fragments.FragmentDirectory
import dagger.Module
import dagger.Provides

@Module
class FragmentReferenceModule {

//    @Provides
//    @PerFragment
//    fun provideFileAdapter(fragmentDirectory: FragmentDirectory): FileAdapter
//            = FileAdapter(fragmentDirectory as RecyclerSelection)
}