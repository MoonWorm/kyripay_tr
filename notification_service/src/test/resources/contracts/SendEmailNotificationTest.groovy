package contracts

import org.apache.http.HttpStatus
import org.springframework.cloud.contract.spec.Contract

Contract.make {

    request {
        method POST()
        url("/api/v1/emailnotifications")
        body("""
        {
            "to"              : "test@email.org",
            "titleTemplateId" : "registration_conf_subj",
            "bodyTemplateId"  : "registration_conf_body",
            "parameters" : {
                "honorific" : "Mr.",
                "firstName" : "Vasia",
                "lastName"  : "Pupkin"
            }
        }        
        """)
    }

    response {
        status(HttpStatus.SC_OK)
        body("""
        {
            "uuid"         : "123e4567-e89b-12d3-a456-426655440000",
            "status"       : "SENT"
        }
        """)
        headers {
            header(
                    'Content-Type', "application/json;charset=UTF-8"
            )
        }
    }
}