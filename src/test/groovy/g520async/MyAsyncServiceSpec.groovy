package g520async

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class MyAsyncServiceSpec extends Specification implements ServiceUnitTest<MyAsyncService>, DataTest{

    def setup() {
    }

    def cleanup() {
    }

    void "test notWaiting"() {
        when:
            def res = service.notWaiting()
        then:
            println res
    }
}
