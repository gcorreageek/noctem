(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordPaymentDialogController', RecordPaymentDialogController);

    RecordPaymentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RecordPayment', 'Card', 'Record', 'User','Principal'];

    function RecordPaymentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RecordPayment, Card, Record, User,Principal) {
        var vm = this;

        vm.recordPayment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cards = Card.query();
        vm.records = Record.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.recordPayment.id !== null) {
                RecordPayment.update(vm.recordPayment, onSaveSuccess, onSaveError);
            } else {
                RecordPayment.save(vm.recordPayment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('noctemApp:recordPaymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        Principal.identity().then(function(account) {
            vm.account = account;
        });


    }
})();
