package g520async

import static grails.async.Promises.task

/**
 * This service is not annotated with @Transactional
 */
class NotTransactionalService {

    /**
     * Add a dog named Leonardo using withTransaction
     */
    def addLeonardoAndFlush() {
        Dog.withTransaction {
            new Dog(name: 'Leonardo').save(flush: true, failOnError: true)
        }
    }

    /**
     * Get a dog named Leonardo in a task, wait and return
     */
    def getLeonardo() {
        def p = task {
            Dog.withTransaction {
                Dog.findByName('Leonardo')
            }
        }
        p.get()
    }
}
