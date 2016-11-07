(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordDeleteController',RecordDeleteController);

    RecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'Record'];

    function RecordDeleteController($uibModalInstance, entity, Record) {
        var vm = this;

        vm.record = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Record.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
