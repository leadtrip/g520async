package g520async

import grails.async.PromiseList
import grails.gorm.transactions.NotTransactional
import grails.gorm.transactions.Transactional

import static grails.async.Promises.task
import static grails.async.Promises.waitAll

@Transactional
class MyAsyncService {

    /**
     * This is pointless because we don't wait for the result
     */
    def notWaiting() {
        log.info('Calculating total')
        def total
        task {
            for(int i = 0 ; i < 9999; i++) {
                total += i
                if(total % 1000 == 0) {
                    log.info("Total so far is $total")
                }
            }
            log.info("Total calculated")
        }
        log.info("Total is $total")
        total
    }

    /**
     * Spin off a task that creates some domain entities, this is a valid scenario
     */
    def notWaitingCreatingDomianEntities() {
        def p = task {
            (0..4).each {i ->
                Dog.withNewSession {                // have to use withNewSession
                    new Dog(name: "Dog$i").save()   // can't use flush: true here, if you do you'll get 'no transaction is in progress'
                }
            }
        }
        p.onError { t ->
            log.info("Error creating dog ${t.message}")
        }
        p.onComplete {
            log.info("Dogs created")
        }
    }

    /**
     * Same as above but able to flush using withTransaction
     * withTransaction uses an existing transaction if available or creates a new one if not
     */
    def notWaitingCreatingDomainEntitiesWithTransaction() {
        def p = task {
            Dog.withTransaction { transactionStatus ->     // now we're using withTransaction we can flush when saving
                log.info("Is new transaction? ${transactionStatus.isNewTransaction()}")
                (0..4).each {i ->
                    new Dog(name: "Dog$i").save(flush: true)
                }
            }
        }
        p.onError { t ->
            log.info("Error creating dog ${t.message}")
        }
        p.onComplete {
            log.info("Dogs created")
        }
    }

    /**
     * Spin off a task, calcuate something and wait
     */
    def waiting() {
        log.info('Calculating total')
        def p = task {
            def total = 0
            for(long i = 0 ; i < 99999999; i++) {
                total += i
                if(i % 10000000 == 0) {
                    log.info("Total so far is $total")
                }
            }
            log.info("Total calculated")
            total
        }
        p.get()
    }

    /**
     * Spin off multiple tasks and wait on the results
     */
    def multiTask() {
        def p1 = task { 2 * 2 }
        def p2 = task { 4 * 4 }
        def p3 = task { 6 * 6 }
        waitAll(p1, p2, p3)
    }

    /**
     * Spin off multiple tasks and wait
     */
    def multiTaskCollated() {
        def sumClosure = { List<Integer> l ->
            log.info "Summing $l"
            l.sum()
        }

        PromiseList res = new PromiseList()
        (1..100).collate(10).each { List<Integer> iList ->
            log.info("Loading $iList")
            res <<  {
                sumClosure(iList)
            }
        }
        res.get()
    }

    /**
     * Add a domain to the database in a task and wait for the task to complete
     */
    def addToDatabase() {
        def p = task {
            Dog.withNewSession {                // new session is required if spinning off a task
                new Dog(name: 'Fido').save()    // can't flush otherwise you'll get 'no transaction is in progress'
            }
        }
        p.get()
    }

    /**
     * Saving an AsyncDog using the async namespace on the gorm entity
     * In this case, as opposed to using the task method directly from the Promises class, the task method binds a new session to the thread for us
     */
    def addToDatabaseGormAsync() {
        def p = AsyncDog.async.task {
            withTransaction {
                new Dog(name: 'Cujo').save(flush: true)     // and we can flush
            }
        }
        p.get()
    }

    /**
     * Update an domain, we need withNewSession in this method but not required in updateName which is called from within this method
     */
    def updateDatabase() {
        def p = task {
            Dog.withNewSession {
                def dog = Dog.findByName('Fido')
                updateName(dog)
            }
        }
        p.get()
    }

    def updateName(dog) {
        dog.name = 'barry'
        dog.save()
    }

    def withTransactionDifferences() {
        def inMethod
        Dog.withTransaction { transactionStatus ->
            inMethod = transactionStatus.isNewTransaction()
        }
        def p = task{
            Dog.withTransaction { transactionStatus ->
                def inTask = transactionStatus.isNewTransaction()
                [inMethod: inMethod, inTask: inTask]
            }
        }
        p.get()
    }

    /**
     * This works fine without newSession, newTransaction etc because the createDog method we're calling is transactional
     * due to the annotation on the class, remove it and this fails
     */
    def taskCallingTransactionalMethod() {
        def p = task {
            createDog('Lassie')
        }
        p.get()
    }

    def createDog(dogName) {
        new Dog(name: dogName).save()
    }

    /**
     * Calling a NotTransactional method we need to wrap the method call in withTransaction
     */
    def taskCallingNonTransactionalMethod() {
        def p = task {
            Dog.withTransaction {
                createNtDog('Fluffy')
            }
        }
        p.get()
    }

    @NotTransactional
    def createNtDog(dogName) {
        new Dog(name: dogName).save()
    }
}
