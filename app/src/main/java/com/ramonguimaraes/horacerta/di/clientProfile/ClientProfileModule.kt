package com.ramonguimaraes.horacerta.di.clientProfile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.data.clientProfile.dataRepository.ClientProfileDataRepository
import com.ramonguimaraes.horacerta.data.clientProfile.dataSource.ClientProfileDataFactory
import com.ramonguimaraes.horacerta.data.clientProfile.dataSource.ClientRemoteDataSource
import com.ramonguimaraes.horacerta.data.clientProfile.mapper.ClientDataMapper
import com.ramonguimaraes.horacerta.domain.clientProfile.repository.ClientProfileRepository
import com.ramonguimaraes.horacerta.domain.clientProfile.useCase.ClientProfileUseCase
import com.ramonguimaraes.horacerta.presentation.clientProfile.viewmode.ClientProfileViewModel
import com.ramonguimaraes.horacerta.presentation.clientProfile.mapper.ClientViewMapper
import com.ramonguimaraes.horacerta.remote.clientProfile.serviceImpl.ClientService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun clientProfileModule() = module {
    factory { FirebaseAuth.getInstance() }
    factory { FirebaseFirestore.getInstance() }

    // Remote
    factory<ClientRemoteDataSource> { ClientService(get(), get()) }

    // Data
    factory { ClientDataMapper() }
    factory { ClientProfileDataFactory(get()) }

    // Domain
    factory<ClientProfileRepository> { ClientProfileDataRepository(get(), get()) }
    factory { ClientProfileUseCase(get()) }

    // Presentation
    factory { ClientViewMapper() }
    viewModel { ClientProfileViewModel(get(), get()) }
}
