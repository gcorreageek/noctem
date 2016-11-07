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
        // vm.add  = function () {
        //     console.log('add');
        //     vm.userGroups.push({name:'',email:''});
        // };
        // vm.usergroups = UserGroup.query();
        UserGroup.query({idgroups: 4}, function(result) {
            vm.userGroups = result;
            console.log(result);
        });




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
            console.log(vm.groups);
            console.log(vm.userGroups);


            // vm.userGroups = [];
            // vm.userGroups[0] = {id:22,name:'gustavito',email:'gustvito@gmai.com',groups:null};
            // vm.groups.userGroups = vm.userGroups;
            vm.groups.userGroups = vm.userGroups;
            // vm.groups.userGroups = [{id:1,name:'gustavito',email:'gustvito@gmai.com',groups:{id:19}}];
            console.log(vm.groups);

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
