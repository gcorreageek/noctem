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
        vm.usergroups = UserGroup.query();
        vm.notifications = Notification.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            console.log(vm.groups)
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
            vm.account = account;
        });


    }
})();
