(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordPaymentDetailController', RecordPaymentDetailController);

    RecordPaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RecordPayment', 'Card', 'Record', 'User'];

    function RecordPaymentDetailController($scope, $rootScope, $stateParams, previousState, entity, RecordPayment, Card, Record, User) {
        var vm = this;

        vm.recordPayment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('noctemApp:recordPaymentUpdate', function(event, result) {
            vm.recordPayment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
