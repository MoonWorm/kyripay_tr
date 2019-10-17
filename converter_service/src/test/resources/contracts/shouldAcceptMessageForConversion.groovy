package contracts

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    input {
        messageFrom("converter-process")
        messageBody('''
            {
              "format": "IDENTITY",
              "payment": {
                "account": {
                  "bankId": "Bank 1",
                  "currency": "USD",
                  "id": "123",
                  "number": "ACC123"
                },
                "id": "ID",
                "transactions": [
                  {
                    "amount": 999,
                    "currency": "USD",
                    "recipient": {
                      "accountNumber": "ACC321",
                      "bankAddress": "some address",
                      "bankName": "Bank 2",
                      "firstName": "John",
                      "id": "321",
                      "lastName": "Doe"
                    }
                  }
                ]
              }
            }
            ''')
    }

    outputMessage {
        sentTo("converter-notifications")
        body("""
          {
            "documentId": "${anyUuid()}",
            "status": "READY"
          }

    """)
    }

}
