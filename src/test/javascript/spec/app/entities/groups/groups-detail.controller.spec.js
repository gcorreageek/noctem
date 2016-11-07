'use strict';

describe('Controller Tests', function() {

    describe('Groups Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGroups, MockUserGroup, MockNotification, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGroups = jasmine.createSpy('MockGroups');
            MockUserGroup = jasmine.createSpy('MockUserGroup');
            MockNotification = jasmine.createSpy('MockNotification');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Groups': MockGroups,
                'UserGroup': MockUserGroup,
                'Notification': MockNotification,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("GroupsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'noctemApp:groupsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
