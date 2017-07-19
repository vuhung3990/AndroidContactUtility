package dev22.com.contactutility.data

import android.util.Log
import io.reactivex.Single
import org.apache.commons.csv.CSVFormat
import java.io.FileReader


/**
 * Created by dev22 on 7/7/17.
 */
class Repository : RepoContract {
    override fun cleanAndImportContact(filePath: String): Single<List<String>> {
        return Single.create({
            val fileReader = FileReader(filePath)
            val records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fileReader)
            val result = arrayListOf<String>()

            if (records.count() > 0) {
                for (record in records) {

                    val map: Map<String, String>? = record.toMap()
                    result.add(map?.get("First Name").toString())
                    Log.d("aaaa", record.toString())
                }
                it.onSuccess(result)
            } else {
                it.onError(Throwable("something when wrong..."))
            }
        })
    }
}
