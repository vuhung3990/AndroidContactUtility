package dev22.com.contactutility.main.di

import dagger.Module
import dagger.Provides
import dev22.com.contactutility.main.MainContract.View

/**
 * Created by dev22 on 6/28/17.
 */
@Module
class MainPresenterModule(val view: View) {
    @Provides
    fun provideView(): View = view
}
