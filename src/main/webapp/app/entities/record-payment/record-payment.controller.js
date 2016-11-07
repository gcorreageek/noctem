(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordPaymentController', RecordPaymentController);

    RecordPaymentController.$inject = ['$scope', '$state', 'RecordPayment'];

    function RecordPaymentController ($scope, $state, RecordPayment) {
        var vm = this;
        
        vm.recordPayments = [];

        loadAll();

        function loadAll() {
            RecordPayment.query(function(result) {
                vm.recordPayments = result;
            });
        }
    }
})();
