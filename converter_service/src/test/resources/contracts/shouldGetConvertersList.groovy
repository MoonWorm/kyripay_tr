package contracts

import org.apache.http.HttpStatus
import org.springframework.cloud.contract.spec.Contract


Contract.make {

    request {
        method GET()
        url("/api/v1/converters")
    }

    response {
        status(HttpStatus.SC_OK)
        body("""
        {
            "TEST" : 
            {
                    "id"         : "${anyAlphaNumeric()}",
                    "name"       : "${nonBlank()}",
                    "description": "${nonBlank()}"
            }
        }
        """)
        headers {
            header(
                    'Content-Type', "application/json;charset=UTF-8"
            )
        }
    }
}

