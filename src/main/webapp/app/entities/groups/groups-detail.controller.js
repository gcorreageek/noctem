(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('GroupsDetailController', GroupsDetailController);

    GroupsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Groups', 'UserGroup', 'Notification', 'User'];

    function GroupsDetailController($scope, $rootScope, $stateParams, previousState, entity, Groups, UserGroup, Notification, User) {
        var vm = this;

        vm.groups = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('noctemApp:groupsUpdate', function(event, result) {
            vm.groups = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
