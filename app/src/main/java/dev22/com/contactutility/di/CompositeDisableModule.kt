package dev22.com.contactutility.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by dev22 on 6/28/17.
 */
@Module
class CompositeDisableModule {
    @Provides
    fun provideCompositeDisable() = CompositeDisposable()
}