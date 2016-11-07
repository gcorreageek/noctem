(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('CardController', CardController);

    CardController.$inject = ['$scope', '$state', 'Card'];

    function CardController ($scope, $state, Card) {
        var vm = this;
        
        vm.cards = [];

        loadAll();

        function loadAll() {
            Card.query(function(result) {
                vm.cards = result;
            });
        }
    }
})();
