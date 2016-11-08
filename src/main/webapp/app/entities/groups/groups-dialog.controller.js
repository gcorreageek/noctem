(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('GroupsDialogController', GroupsDialogController);

    GroupsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Groups', 'UserGroup', 'Notification', 'User','Principal'];

    function GroupsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Groups, UserGroup, Notification, User,Principal) {
        var vm = this;

        vm.groups = entity;
        vm.clear = clear;
        vm.save = save;
        vm.add  = function () {
            vm.userGroups.push({name:'',email:''});
        };
        vm.delete  = function ( index) {
            vm.userGroups.splice(index,1);
        };
        UserGroup.query({idgroups: vm.groups.id}, function(result) {
            vm.userGroups = result;
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }


        function save () {
            vm.isSaving = true;
            vm.groups.userGroups = vm.userGroups;
            if (vm.groups.id !== null) {
                Groups.update(vm.groups, onSaveSuccess, onSaveError);
            } else {
                Groups.save(vm.groups, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('noctemApp:groupsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        Principal.identity().then(function(account) {
            User.get({login: account.login}, function(result) {
                vm.groups.user = result;
            });
        });


    }
})();
