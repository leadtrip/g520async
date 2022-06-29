package g520async

class MyStandardController {

    def myAsyncService

    def index() {

    }

    def notWaiting() {
        render template: 'results', model:[result: myAsyncService.notWaiting()]
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
}
