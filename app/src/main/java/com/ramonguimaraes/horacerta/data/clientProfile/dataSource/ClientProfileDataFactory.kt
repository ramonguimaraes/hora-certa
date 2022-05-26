package com.ramonguimaraes.horacerta.data.clientProfile.dataSource

class ClientProfileDataFactory(
    private val remoteDataSource: ClientRemoteDataSource
) {
    fun getRemote() = remoteDataSource
}
