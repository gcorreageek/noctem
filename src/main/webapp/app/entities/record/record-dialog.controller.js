(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordDialogController', RecordDialogController);

    RecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Record', 'RecordItem', 'RecordPayment', 'User','Principal'];

    function RecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Record, RecordItem, RecordPayment, User,Principal) {
        var vm = this;

        vm.record = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.recorditems = RecordItem.query();
        vm.recordpayments = RecordPayment.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.record.id !== null) {
                console.log(vm.record);
                Record.update(vm.record, onSaveSuccess, onSaveError);
            } else {
                vm.record.date = new Date();
                Record.save(vm.record, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('noctemApp:recordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        Principal.identity().then(function(account) {
            User.get({login: account.login}, function(result) {
                vm.record.user =result;
            });
        });
    }
})();
