(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('GroupsController', GroupsController);

    GroupsController.$inject = ['$scope', '$state', 'Groups'];

    function GroupsController ($scope, $state, Groups) {
        var vm = this;
        
        vm.groups = [];

        loadAll();

        function loadAll() {
            Groups.query(function(result) {
                vm.groups = result;
            });
        }
    }
})();
