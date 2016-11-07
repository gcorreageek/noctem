(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordItemDetailController', RecordItemDetailController);

    RecordItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RecordItem', 'Record'];

    function RecordItemDetailController($scope, $rootScope, $stateParams, previousState, entity, RecordItem, Record) {
        var vm = this;

        vm.recordItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('noctemApp:recordItemUpdate', function(event, result) {
            vm.recordItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
