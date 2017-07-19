package dev22.com.contactutility.di

import dagger.Module
import dagger.Provides
import org.supercsv.cellprocessor.Optional
import org.supercsv.cellprocessor.ParseDate
import org.supercsv.cellprocessor.constraint.NotNull
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty
import org.supercsv.cellprocessor.constraint.StrRegEx
import org.supercsv.cellprocessor.ift.CellProcessor

/**
 * Created by dev22 on 7/7/17.
 */
@Module
class CellCSVProcessorModule() {
    @Provides
    fun provideCellCSVProcessor(): Array<CellProcessor> {
        val emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+" // just an example, not very robust!
        StrRegEx.registerMessage(emailRegex, "must be a valid email address")

        return arrayOf<CellProcessor>(
                StrNotNullOrEmpty(), // display Name
                NotNull(), // number 1
                Optional(), // number 2
                Optional(), // number 3
                Optional(), // number 4
                Optional(StrRegEx(emailRegex)), // email
                Optional(ParseDate("dd/MM/yyyy")), // birthDate
                Optional(), // address
                Optional(), // job title
                Optional(), // company
                Optional(), // state or provide
                Optional(), // city
                Optional() // country
        )
    }
}
