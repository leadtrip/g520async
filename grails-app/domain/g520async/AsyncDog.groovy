package g520async

import grails.gorm.async.AsyncEntity
import groovy.transform.ToString

@ToString
class AsyncDog implements AsyncEntity<AsyncDog>{

    String name

    static constraints = {
    }
}
