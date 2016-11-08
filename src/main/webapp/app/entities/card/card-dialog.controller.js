(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('CardDialogController', CardDialogController);

    CardDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Card', 'RecordPayment', 'User','Principal'];

    function CardDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Card, RecordPayment, User,Principal) {
        var vm = this;

        vm.card = entity;
        vm.clear = clear;
        vm.save = save;
        vm.recordpayments = RecordPayment.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.card.id !== null) {
                Card.update(vm.card, onSaveSuccess, onSaveError);
            } else {
                Card.save(vm.card, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('noctemApp:cardUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        Principal.identity().then(function(account) {
            User.get({login: account.login}, function(result) {
                vm.card.user =result;
            });
        });

    }
})();
