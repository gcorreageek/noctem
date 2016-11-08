(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordPaymentController', RecordPaymentController);

    RecordPaymentController.$inject = ['$scope', '$state', 'RecordPayment','Principal'];

    function RecordPaymentController ($scope, $state, RecordPayment,Principal) {
        var vm = this;

        vm.recordPayments = [];

        loadAll();

        function loadAll() {
            RecordPayment.query(function(result) {
                vm.recordPayments = result;
            });
        }
        Principal.identity().then(function(account) {
            vm.account = account;
        });

    }
})();
