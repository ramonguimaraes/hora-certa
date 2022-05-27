package com.ramonguimaraes.horacerta.remote.companyRegistration

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.data.companyRegistration.CompanyRemoteDataSource
import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Address
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import com.ramonguimaraes.horacerta.remote.clientProfile.serviceImpl.ClientService
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CompanyService(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CompanyRemoteDataSource {

    override suspend fun saveCompany(company: Company) = flow {
        val companyId = auth.currentUser?.uid
        val docRef = db.collection(COMPANY_COLLECTION).document(companyId.toString())

        val addressId = saveAddress(company.address!!)

        if (addressId.isNullOrBlank()) {
            emit(Result.Failure("Falha ao cadastrar endereço"))
            return@flow
        }

        company.addressRef = addressId
        // TODO: MELHORAR O TRATAMENTO DE ERROS E RESOLVER A MENSAGEM PARA CADA TIPO DE ERRO
        val response = suspendCoroutine<Result<String>> { continuation ->
            docRef.set(company.hashMap()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "deu certo, salvou a company")
                    continuation.resume(Result.Success("Empresa cadastrada com sucesso!"))
                } else {
                    continuation.resume(Result.Failure("Falha ao cadastrar empresa"))
                }
            }
        }

        emit(response)
        return@flow
    }


    private suspend fun saveAddress(address: Address): String? {
        val docRef = db.collection(ADDRESS_COLLECTION).document()

        return try {
            suspendCoroutine { continuation ->
                docRef.set(address).addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(docRef.id)
                    } else {
                        continuation.resumeWithException(Throwable(it.exception))
                    }
                }
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log(ClientService.CRASHLYTICS_KEY)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    companion object {
        const val COMPANY_COLLECTION = "company"
        const val ADDRESS_COLLECTION = "address"
    }
}
