(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordItemDeleteController',RecordItemDeleteController);

    RecordItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'RecordItem'];

    function RecordItemDeleteController($uibModalInstance, entity, RecordItem) {
        var vm = this;

        vm.recordItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RecordItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
