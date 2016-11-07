(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordDetailController', RecordDetailController);

    RecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Record', 'RecordItem', 'RecordPayment', 'User'];

    function RecordDetailController($scope, $rootScope, $stateParams, previousState, entity, Record, RecordItem, RecordPayment, User) {
        var vm = this;

        vm.record = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('noctemApp:recordUpdate', function(event, result) {
            vm.record = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
