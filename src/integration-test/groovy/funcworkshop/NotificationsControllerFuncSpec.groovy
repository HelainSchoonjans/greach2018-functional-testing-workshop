package funcworkshop

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Value
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class NotificationsControllerFuncSpec extends Specification {

    NotificationService notificationService

    @Value('${local.server.port}')
    Integer port

    @Shared
    RestBuilder restBuilder = new RestBuilder()

    String getBaseUrl() {
        "http://localhost:${port}"
    }

    def setup() {
    }

    def cleanup() {
    }

    def "It fail when calling the /notifications endpoint, because of the mailService"() {
        when:
        def response = restBuilder.get baseUrl + '/notification'

        then:
        //thrown(RuntimeException)

        //and:
        response.status == 500
    }

    def "It can call the notifications endpoints, sending a mail"() {
        given:
        def mailServiceMock = Mock(MailService)
        notificationService.mailService = mailServiceMock

        when:
        def response = restBuilder.get baseUrl + '/notification'

        then:
        1 * mailServiceMock.send()

        and:
        response.status == 200
    }
}