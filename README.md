# kyripay_tr
KyriPay microservices training project

## Business requirements:

###. As a *Customer*:
1. I would like to manage all my payments in more convnient way, so I can provide minimal required personal information (First, Last, Middle name, mobile, email, address) and register in the system.
2. I would like to manage (create/update/delete) my bank accounts and prepare accounts base within the system, so I can choose a required one much faster during placing numerous payments in future. Each account should have the next properties: bank details (including format and transport line), account number, bank connection details, payment format details. Bank details should be able to choose from the list of banks that is known by the system.
3. I would like to manage (create/update/delete) my base of recipients within the system, so I can choose a required one much faster during placing numerous payments in future. Each recipient should contains the next information: recipient person info, recipient bank details, recipient account number. 
4. I would like to manage (create/update/delete) my base of payment templates within the system, so I can use one of them during placing numerous payments in future to save a reasonable amount of time and do not fill set of fields each time with the same values. Each payment template should contain source account details and recipient details. Amount of payment could be different per each new payentm thus it should be specified during placing a new payment.
5. Given one of my account numbers (optional), payment template (optional), recipient (optional) and specified amount of money. In case none of mentioned entity (account, payment template, recipient) was used from my base there should be a prompt to save it there after all required fields are entered.
I would like to place a new payment. After new payment is placed it should be able to "Edit", "Delete" or "Send" it. Initial payment status is set to "Created".
6. Given some created payment and payment is not sent yet. I would like to be able to delete it.
6. Given some created payment and payment is not sent yet. I would like to be able to update its details (account, recipient, amount).
7. Given some created payment. I would like to be able to send it. Once payment was sent it's no longer able to "Delete", "Edit" or "Send" it but there should be a new option - "Resend" that is actuially places a new payment using the same payment details including amount. Future enhancement: add payment scheduling here. As soon as payment is sent, payment status should be changed to "In progress".
8. Given some created and sent payment. Payment should be first transformed into an appropriate format that is used by a customer bank. Then, it should be sent to a 3rd party bank of the recipient by using configured transport line that is used by a customer bank. As soon as payment was successfully sent its status should be changed to "Sent"
9. Given a have a set of payments. I would like to review the statuses of all my payments and apply some custom search filters to gather some required information that might be helpfull for some statistics or analysis purposes.

###. As a *Manager*:
1. Given some registered set of customers. I would like to disable and enable some of them. Disabled used can't perform any action in the system as soon as it was disabled.
2. Given a have a set of customers with some payments. I would like to apply some custom search filters to gather some required information that might be helpfull for some statistics or analysis purposes. Personal and some part of payment information should be restricted and manager must not be able to see it.
3. I'd like to add a new bank into the system. Along with bank details, bank format and transport line should be also configured.
