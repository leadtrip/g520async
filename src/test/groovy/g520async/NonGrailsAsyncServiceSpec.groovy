package g520async

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class NonGrailsAsyncServiceSpec extends Specification implements ServiceUnitTest<NonGrailsAsyncService>{

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
