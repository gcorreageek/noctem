(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordDetailController', RecordDetailController);

    RecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Record', 'RecordItem', 'RecordPayment', 'User','Principal'];

    function RecordDetailController($scope, $rootScope, $stateParams, previousState, entity, Record, RecordItem, RecordPayment, User,Principal) {
        var vm = this;

        vm.record = entity;
        vm.previousState = previousState.name;
        RecordItem.query({idrecord: vm.record.id}, function(result) {
            vm.recordItems = result;
        });

        var unsubscribe = $rootScope.$on('noctemApp:recordUpdate', function(event, result) {
            vm.record = result;
        });
        $scope.$on('$destroy', unsubscribe);
        Principal.identity().then(function(account) {
            vm.account = account;
        });
    }
})();
