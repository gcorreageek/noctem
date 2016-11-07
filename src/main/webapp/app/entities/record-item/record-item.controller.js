(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('RecordItemController', RecordItemController);

    RecordItemController.$inject = ['$scope', '$state', 'RecordItem'];

    function RecordItemController ($scope, $state, RecordItem) {
        var vm = this;
        
        vm.recordItems = [];

        loadAll();

        function loadAll() {
            RecordItem.query(function(result) {
                vm.recordItems = result;
            });
        }
    }
})();
