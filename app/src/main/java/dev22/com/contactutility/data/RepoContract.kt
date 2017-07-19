package dev22.com.contactutility.data

import io.reactivex.Single

/**
 * Created by dev22 on 7/7/17.
 */
interface RepoContract {
    fun cleanAndImportContact(it: String): Single<List<String>>
}