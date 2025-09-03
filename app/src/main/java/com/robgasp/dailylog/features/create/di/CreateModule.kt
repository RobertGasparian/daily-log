package com.robgasp.dailylog.features.create.di

import com.robgasp.dailylog.features.create.CreateLogUIStateValidator
import com.robgasp.dailylog.features.create.CreateViewModel
import com.robgasp.dailylog.core.misc.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CreateModule {

    @Provides
    fun provideLogValidator(impl: CreateLogUIStateValidator): Validator<CreateViewModel.UIState> = impl
}
