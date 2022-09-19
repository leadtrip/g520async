package g520async

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class NotTransactionalServiceSpec extends Specification implements ServiceUnitTest<NotTransactionalService>{

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
