(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordItemDialogController', RecordItemDialogController);

    RecordItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RecordItem', 'Record'];

    function RecordItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RecordItem, Record) {
        var vm = this;

        vm.recordItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.records = Record.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.recordItem.id !== null) {
                RecordItem.update(vm.recordItem, onSaveSuccess, onSaveError);
            } else {
                RecordItem.save(vm.recordItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('noctemApp:recordItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
