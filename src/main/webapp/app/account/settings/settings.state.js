(function() {
    'use strict';

    angular
        .module('noctemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('settings', {
            parent: 'account',
            url: '/settings',
            data: {
                authorities: ['ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_SUPERADMIN'],
                pageTitle: 'global.menu.account.settings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/settings/settings.html',
                    controller: 'SettingsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('settings');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
