package com.ramonguimaraes.horacerta.presenter.companyProfile.mapper

import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.presenter.companyProfile.model.CompanyProfileView
import com.ramonguimaraes.horacerta.presenter.viewUtils.viewMapper.ViewMapper


class CompanyProfileViewMapper : ViewMapper<CompanyProfileView, CompanyProfile>() {

    override fun mapToView(model: CompanyProfile): CompanyProfileView {
        return CompanyProfileView(
            id = model.id,
            companyUid = model.companyUid,
            companyName = model.companyName,
            cnpj = model.cnpj,
            phoneNumber = model.phoneNumber,
            companySegment = model.companySegment,
            photoUri = model.photoUri,
            rua = model.rua,
            bairro = model.bairro,
            numero = model.numero,
            cidade = model.cidade,
            complemento = model.complemento,
            semNumero = model.semNumero,
            latitude = model.latitude,
            longitude = model.longitude
        )
    }

    override fun mapFromView(view: CompanyProfileView): CompanyProfile {
        return CompanyProfile(
            id = view.id,
            companyUid = view.companyUid,
            companyName = view.companyName,
            cnpj = view.cnpj,
            phoneNumber = view.phoneNumber,
            companySegment = view.companySegment,
            photoUri = view.photoUri,
            rua = view.rua,
            bairro = view.bairro,
            numero = view.numero,
            cidade = view.cidade,
            complemento = view.complemento,
            semNumero = view.semNumero,
            latitude = view.latitude,
            longitude = view.longitude
        )
    }
}
