# kyripay_tr
KyriPay microservices training project

## Business requirements:

### As a *Customer*:
1. I would like to manage all my payments in more convnient way, so I can provide minimal required personal information (First, Last, Middle name, mobile, email, address) and register in the system. After registration I should be able to login into system and use it and logout to ensure that nobody else can't use it but me.
2. I would like to manage (create/update/delete) my bank accounts and prepare accounts base within the system, so I can choose a required one much faster during placing numerous payments in future. Each account should have the next properties: bank details (including format and transport line), account number, bank connection details, payment format details. Bank details should be able to choose from the list of banks that is known by the system.
3. I would like to manage (create/update/delete) my base of recipients within the system, so I can choose a required one much faster during placing numerous payments in future. Each recipient should contains the next information: recipient person info, recipient bank details, recipient account number. 
4. I would like to manage (create/update/delete) my base of payment templates within the system, so I can use one of them during placing numerous payments in future to save a reasonable amount of time and do not fill set of fields each time with the same values. Each payment template should contain source account details and recipient details. Amount of payment could be different per each new payentm thus it should be specified during placing a new payment.
5. Given one of my account numbers, payment template, recipient and specified amount of money. In case none of mentioned entity (account, payment template, recipient) was used from my base there should be a prompt to save it there after all required fields are entered.
I would like to place and send a new payment. After new payment is placed it status should be initially set to "Created". After it's created it can be automatically taken for processing by the system. As soon as the payment processing is started its status is changed to "Processing". When the payment processing is started it should be firstly transformed into an appropriate format that is used by a customer bank. Then, it should be sent to a 3rd party bank of the recipient by using configured transport line that is used by a customer bank. As soon as payment was successfully sent its status should be changed to "Sent". Recipient's bank first respond with an acknowledgement that payment request is received. Payment status is changed to "Received". Then, 3rd-party bank can either respond with "Rejected" or "Accepted" status depending on whether payment was rejected or successfully transferred to the recipient's account. In case some error happens on any of the step during payment processing - "Failed" status should be set and payment should be excluded out of the system from the payments processing.
6. Given a have a set of payments. I would like to review the statuses of all my payments and apply some custom search filters to gather some required information that might be helpfull for some statistics or analysis purposes.

### As a *Manager*:
1. Given some registered set of customers. I would like to disable and enable some of them. Disabled used can't perform any action in the system as soon as it was disabled.
2. Given a have a set of customers with some payments. I would like to apply some custom search filters to gather some required information that might be helpfull for some statistics or analysis purposes. Personal and some part of payment information should be restricted and manager must not be able to see it.
3. I'd like to add a new bank into the system. Along with bank details, bank format and transport line should be also configured.

## Logical view:

![kyripay](https://user-images.githubusercontent.com/475392/55720241-082ec880-5a09-11e9-8200-a3b490e132a3.png)

## Logical with with bounded contexts:

![kyripay_contexts](https://user-images.githubusercontent.com/475392/55721330-c05d7080-5a0b-11e9-8716-576a9b319123.png)

## Microservices decomposition:

![ms](https://user-images.githubusercontent.com/475392/55722175-2c40d880-5a0e-11e9-8288-84d4f5f0be33.png)
