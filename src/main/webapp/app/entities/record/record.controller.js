(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordController', RecordController);

    RecordController.$inject = ['$scope', '$state', 'Record'];

    function RecordController ($scope, $state, Record) {
        var vm = this;
        
        vm.records = [];

        loadAll();

        function loadAll() {
            Record.query(function(result) {
                vm.records = result;
            });
        }
    }
})();
