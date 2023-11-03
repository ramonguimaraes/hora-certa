package com.ramonguimaraes.horacerta.data.cashFlow

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ramonguimaraes.horacerta.domain.cashFlow.CashRegister
import com.ramonguimaraes.horacerta.domain.cashFlow.repository.CashFlowRepository
import com.ramonguimaraes.horacerta.domain.cashFlow.toCashRegister
import com.ramonguimaraes.horacerta.domain.cashFlow.toHashMap
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.services.model.Service
import kotlinx.coroutines.tasks.await
import java.util.Date

class CashFlowRepositoryImpl(private val db: FirebaseFirestore) : CashFlowRepository {

    override suspend fun save(cashRegister: CashRegister): Resource<CashRegister> {
        return try {
            db.collection(COLLECTION).add(cashRegister.toHashMap()).await()
            return Resource.Success(cashRegister)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun load(companyUid: String): Resource<List<CashRegister>> {
        return try {
            val snapshots =
                db.collection(COLLECTION).whereEqualTo("companyUid", companyUid).get().await()
            val resultList: MutableList<CashRegister> = mutableListOf()

            snapshots.forEach {
                resultList.add(it.toCashRegister())
            }

            Resource.Success(resultList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun load(
        companyUid: String,
        dateRange: Pair<Date, Date>
    ): Resource<List<CashRegister>> {
        return try {
            val snapshots = db.collection(COLLECTION)
                .whereGreaterThan("date", Timestamp(dateRange.first))
                .whereLessThan("date", Timestamp(dateRange.second))
                .whereEqualTo("companyUid", companyUid)
                .get().await()

            val resultList: MutableList<CashRegister> = mutableListOf()

            snapshots.forEach {
                resultList.add(
                    it.toCashRegister().copy(
                        services = loadServices(it)
                    )
                )
            }

            Resource.Success(resultList)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    private suspend fun loadServices(queryDocumentSnapshot: QueryDocumentSnapshot): List<Service> {
        val servicesRefs = queryDocumentSnapshot.get("services") as List<*>
        val servicesRes = db.collection("services").whereIn("id", servicesRefs).get().await()
        val services = mutableListOf<Service>()
        servicesRes.forEach { snapShot ->
            val service = snapShot.toObject(Service::class.java)
            service.id = snapShot.id
            services.add(service)
        }
        return services
    }

    private companion object {
        const val COLLECTION = "cashRegisters"
    }
}
