(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('CardDetailController', CardDetailController);

    CardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Card', 'RecordPayment', 'User'];

    function CardDetailController($scope, $rootScope, $stateParams, previousState, entity, Card, RecordPayment, User) {
        var vm = this;

        vm.card = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('noctemApp:cardUpdate', function(event, result) {
            vm.card = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
