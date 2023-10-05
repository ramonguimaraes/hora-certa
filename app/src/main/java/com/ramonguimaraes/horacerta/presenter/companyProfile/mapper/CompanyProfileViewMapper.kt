package com.ramonguimaraes.horacerta.presenter.companyProfile.mapper

import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.presenter.companyProfile.model.CompanyProfileView
import com.ramonguimaraes.horacerta.presenter.viewMapper.ViewMapper


class CompanyProfileViewMapper : ViewMapper<CompanyProfileView, CompanyProfile>() {

    override fun mapToView(model: CompanyProfile): CompanyProfileView {
        return CompanyProfileView(
            id = model.id,
            companyUid = model.companyUid,
            companyName = model.companyName,
            cnpj = model.cnpj,
            phoneNumber = model.phoneNumber,
            companySegment = model.companySegment,
            photoUri = model.photoUri
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
            photoUri = view.photoUri
        )
    }
}
