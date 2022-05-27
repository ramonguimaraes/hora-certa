package com.ramonguimaraes.horacerta.presentation.companyRegistration.mapper

import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Address
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import com.ramonguimaraes.horacerta.presentation.base.ViewMapper
import com.ramonguimaraes.horacerta.presentation.companyRegistration.model.AddressView
import com.ramonguimaraes.horacerta.presentation.companyRegistration.model.CompanyView

class CompanyViewMapper : ViewMapper<CompanyView, Company> {
    override fun toView(domain: Company): CompanyView {
        return CompanyView(
            domain.uuid,
            domain.companyName,
            domain.email,
            AddressViewMapper().toView(domain.address!!)
        )
    }

    override fun fromView(view: CompanyView): Company {
        return Company(
            view.uuid,
            view.companyName,
            view.email,
            AddressViewMapper().fromView(view.address!!)
        )
    }
}

class AddressViewMapper : ViewMapper<AddressView, Address> {
    override fun toView(domain: Address): AddressView {
        return AddressView(
            domain.country,
            domain.state,
            domain.city,
            domain.district,
            domain.street,
            domain.number,
            domain.zipCode,
            domain.latitude,
            domain.longitude,
            domain.complement
        )
    }

    override fun fromView(view: AddressView): Address {
        return Address(
            view.country,
            view.state,
            view.city,
            view.district,
            view.street,
            view.number,
            view.zipCode,
            view.latitude,
            view.longitude,
            view.complement
        )
    }
}
