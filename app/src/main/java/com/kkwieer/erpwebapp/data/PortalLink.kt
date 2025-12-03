package com.kkwieer.erpwebapp.data

import java.util.UUID

data class PortalLink(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val url: String,
    val isDefault: Boolean = false
) {
    companion object {
        val DEFAULT_LINKS = listOf(
            PortalLink(
                id = "lms_portal",
                name = "LMS Portal",
                url = "http://era.mkcl.org/lms/#/15477477481473922139",
                isDefault = true
            ),
            PortalLink(
                id = "mobile_app_dev",
                name = "Mobile App Development Course",
                url = "https://eranx.mkcl.org/learner/login",
                isDefault = true
            ),
            PortalLink(
                id = "aerp_login",
                name = "AERP Login",
                url = "https://aerp.kkwagh.edu.in",
                isDefault = true
            )
        )
    }
}
