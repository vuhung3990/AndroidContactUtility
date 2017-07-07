package dev22.com.contactutility.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by dev22 on 7/7/17.
 */
@Module
@Singleton
class RepositoryModule {
    @Provides
    fun provideRepository() = Repository()
}