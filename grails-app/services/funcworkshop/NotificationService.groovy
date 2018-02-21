package funcworkshop

import grails.gorm.transactions.Transactional

@Transactional
class NotificationService {

    MailService mailService

    def sendMail() {
        mailService.send()
    }
}
