package g520async

import grails.gorm.transactions.Transactional
import java.util.concurrent.Executors


/**
 * A service that provides async functionality but not using grails features
 */
@Transactional
class NonGrailsAsyncService {

    static final THREAD_POOL_SIZE = 11

    /**
     * Adds some rows to the database, note the requirement to use withTransaction or similar
     */
    def addAbunchOfDatabaseEntries() {
        def c = {
            Dog.withTransaction {
                10.times {
                    def dog = new Dog(name: UUID.randomUUID()).save()
                    log.info("Dog added with ID ${dog.id}, name ${dog.name}")
                }
            }
        }
        executeInThreadPool( c )
    }

    /**
     * Just prints to the console with the thread ID
     */
    def printAbunchOfNumbers() {
        def c = {
            10.times {
                log.info( "${Thread.currentThread().getName()} $it" )
            }
        }
        executeInThreadPool( c )
    }

    /**
     * Supply a closure to be run by THREAD_POOL_SIZE number of threads with no blocking
     */
    def executeInThreadPool(Closure task) {
        List<Runnable> tasks = []
        THREAD_POOL_SIZE.times { i ->
            tasks.add({
                try {
                    task.call()
                } catch (Exception e) {
                    log.error("Error running task ", e)
                }
            } as Runnable)
        }

        log.info("Tasks built")

        tasks.collect { Executors.newFixedThreadPool(THREAD_POOL_SIZE).submit(it) }
    }

}
