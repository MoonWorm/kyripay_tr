package contracts

import org.apache.http.HttpStatus
import org.springframework.cloud.contract.spec.Contract

Contract.make {

    request {
        method POST()
        headers {
            header(
                    'Content-Type', "application/json;charset=UTF-8"
            )
        }
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
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
        body("""
        {
            "uuid"         : "${anyUuid()}",
            "status"       : "${anyOf('SENT', 'FAILED')}"
        }
        """)
    }
}