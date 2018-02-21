package funcworkshop

import grails.converters.JSON

class NotificationController {
	static responseFormats = ['json']

    NotificationService notificationService
	
    def index() {
        notificationService.sendMail()
        render [:] as JSON
    }
}
