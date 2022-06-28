package g520async

import grails.async.PromiseList
import grails.gorm.transactions.Transactional

import static grails.async.Promises.task
import static grails.async.Promises.waitAll

@Transactional
class MyAsyncService {

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

    def multiTask() {
        def p1 = task { 2 * 2 }
        def p2 = task { 4 * 4 }
        def p3 = task { 6 * 6 }
        waitAll(p1, p2, p3)
    }

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

    def addToDatabase() {
        def p = task {
            Dog.withNewSession {                // new session is required if spinning off a task
                new Dog(name: 'Fido').save()
            }
        }
        p.get()
    }

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
}
