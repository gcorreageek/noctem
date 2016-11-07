'use strict';

describe('Controller Tests', function() {

    describe('RecordPayment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRecordPayment, MockCard, MockRecord, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRecordPayment = jasmine.createSpy('MockRecordPayment');
            MockCard = jasmine.createSpy('MockCard');
            MockRecord = jasmine.createSpy('MockRecord');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'RecordPayment': MockRecordPayment,
                'Card': MockCard,
                'Record': MockRecord,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("RecordPaymentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'noctemApp:recordPaymentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
