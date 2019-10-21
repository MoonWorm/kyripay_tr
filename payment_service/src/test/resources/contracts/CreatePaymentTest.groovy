package contracts

import org.apache.http.HttpStatus
import org.springframework.cloud.contract.spec.Contract

Contract.make {

    request {
        method POST()
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
            header('userId', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11')
        }
        url("/api/v1/payments")
        body("""
        {
          "paymentDetails": {
            "amount": {
              "amount": 500,
              "currency": "USD"
            },
            "bankId": 1,
            "accountNumber": "IBAN123",
            "recipientInfo": {
              "firstName": "Vasia",
              "lastName": "Pupkin",
              "bankUrn": "0000/00222/0XXXX",
              "bankName": "Fake Bank Inc",
              "bankAddress": "Main str. 1-1",
              "accountNumber": "IBAN321"
            }
          }
        }   
        """)
    }

    response {
        status(HttpStatus.SC_OK)
        body("""
        {
          "id": "${anyNumber()}",
          "status": "${anyOf('CREATED', 'PROCESSING', 'COMPLETED')}",
          "paymentDetails": {
            "amount": {
              "amount": "${anyNumber()}",
              "currency": "${anyOf('USD', 'EUR', 'BYN', 'RUR')}"
            },
            "bankId": "${anyNumber()}",
            "accountNumber": "${anyNonBlankString()}",
            "recipientInfo": {
              "firstName": "${anyNonBlankString()}",
              "lastName": "${anyNonBlankString()}",
              "bankUrn": "${anyNonBlankString()}",
              "bankName": "${anyNonBlankString()}",
              "bankAddress": "${anyNonBlankString()}",
              "accountNumber": "${anyNonBlankString()}"
            }
          },
          "createdOn": "${anyNumber()}"
        } 
        """)
    }
}