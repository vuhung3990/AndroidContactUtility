package dev22.com.contactutility.main.di

import dagger.Component
import dev22.com.contactutility.di.CompositeDisableModule
import dev22.com.contactutility.main.MainActivity

/**
 * Created by dev22 on 6/28/17.
 */
@Component(modules = arrayOf(MainPresenterModule::class, CompositeDisableModule::class))
internal interface MainComponent {
    fun inject(activity: MainActivity)
}
