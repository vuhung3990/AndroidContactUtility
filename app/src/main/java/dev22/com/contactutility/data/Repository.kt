package dev22.com.contactutility.data

import io.reactivex.Single

/**
 * Created by dev22 on 7/7/17.
 */
class Repository : RepoContract {
    override fun cleanAndImportContact(): Single<List<Any>> {
        return Single.create {

        }
    }
}