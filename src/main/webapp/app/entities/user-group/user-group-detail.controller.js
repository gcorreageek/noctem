(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('UserGroupDetailController', UserGroupDetailController);

    UserGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserGroup', 'Groups'];

    function UserGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, UserGroup, Groups) {
        var vm = this;

        vm.userGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('noctemApp:userGroupUpdate', function(event, result) {
            vm.userGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
