(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordPaymentDeleteController',RecordPaymentDeleteController);

    RecordPaymentDeleteController.$inject = ['$uibModalInstance', 'entity', 'RecordPayment'];

    function RecordPaymentDeleteController($uibModalInstance, entity, RecordPayment) {
        var vm = this;

        vm.recordPayment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RecordPayment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
