package g520async

class MyStandardController {

    def myAsyncService
    def nonGrailsAsyncService

    def index() {

    }

    def notWaiting() {
        render template: 'results', model:[result: myAsyncService.notWaiting()]
    }

    def notWaitingCreatingDomianEntities() {
        myAsyncService.notWaitingCreatingDomianEntities()
        redirect action: 'allDogs'
    }

    def notWaitingCreatingDomainEntitiesWithTransaction() {
        myAsyncService.notWaitingCreatingDomainEntitiesWithTransaction()
        redirect action: 'allDogs'
    }

    def allDogs() {
        render template: 'results', model: [result: Dog.all*.name]
    }

    def waiting() {
        render template: 'results', model:[result: myAsyncService.waiting()]
    }

    def multiTask() {
        render template: 'results', model:[result: myAsyncService.multiTask()]
    }

    def multiTaskCollated() {
        render template: 'results', model:[result: myAsyncService.multiTaskCollated()]
    }

    def addToDatabase() {
        render template: 'results', model:[result: myAsyncService.addToDatabase()]
    }

    def updateDatabase() {
        render template: 'results', model:[result: myAsyncService.updateDatabase()]
    }

    def addToDatabaseGormAsync() {
        render template: 'results', model:[result: myAsyncService.addToDatabaseGormAsync()]
    }

    def withTransactionDifferences() {
        render template: 'results', model: [result: "Is transaction new? ${myAsyncService.withTransactionDifferences()}"]
    }

    def taskCallingTransactionalMethod() {
        render template: 'results', model:[result: myAsyncService.taskCallingTransactionalMethod()]
    }

    def taskCallingNonTransactionalMethod() {
        render template: 'results', model:[result: myAsyncService.taskCallingNonTransactionalMethod()]
    }

    def printAbunchOfNumbers() {
        render template: 'results', model: [result: nonGrailsAsyncService.printAbunchOfNumbers()]
    }

    def addAbunchOfDatabaseEntries() {
        render template: 'results', model: [result: nonGrailsAsyncService.addAbunchOfDatabaseEntries()]
    }
}
