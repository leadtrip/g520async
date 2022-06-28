package g520async

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DogSpec extends Specification implements DomainUnitTest<Dog> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
