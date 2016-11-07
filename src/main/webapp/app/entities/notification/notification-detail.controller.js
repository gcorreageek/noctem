(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('NotificationDetailController', NotificationDetailController);

    NotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notification', 'Groups', 'Event', 'User'];

    function NotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Notification, Groups, Event, User) {
        var vm = this;

        vm.notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('noctemApp:notificationUpdate', function(event, result) {
            vm.notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
