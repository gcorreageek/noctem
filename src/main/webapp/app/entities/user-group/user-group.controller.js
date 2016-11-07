(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('UserGroupController', UserGroupController);

    UserGroupController.$inject = ['$scope', '$state', 'UserGroup'];

    function UserGroupController ($scope, $state, UserGroup) {
        var vm = this;
        
        vm.userGroups = [];

        loadAll();

        function loadAll() {
            UserGroup.query(function(result) {
                vm.userGroups = result;
            });
        }
    }
})();
