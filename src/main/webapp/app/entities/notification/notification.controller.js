(function() {
    'use strict';

    angular
        .module('noctemApp')
        .controller('NotificationController', NotificationController);

    NotificationController.$inject = ['$scope', '$state', 'Notification','AlertService','$translate'];

    function NotificationController ($scope, $state, Notification,AlertService,$translate) {
        var vm = this;


        vm.notifications = [];
        vm.setActive = setActive;
        vm.loadAll = loadAll;

        vm.loadAll();
        vm.clear = clear;


        function loadAll() {
            Notification.query(function(result) {
                vm.notifications = result;
            });
        }
        function clear () {
            vm.notification = {
                id: null,  send: null, groups: null, event: null, user: null
            };
        }
        function setActive (notification, isActivated) {
            if(isActivated){
                notification.send = isActivated;
                Notification.update(notification, function () {
                    vm.loadAll();
                    vm.clear();
                });
            }else{
                AlertService.error($translate.instant('noctemApp.notification.sendAlreadyAlert'));
            }
        }
    }
})();
