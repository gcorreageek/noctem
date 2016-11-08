(function() {
    'use strict';

    angular
        .module('noctemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('record-payment', {
            parent: 'entity',
            url: '/record-payment',
            data: {
                authorities: ['ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_SUPERADMIN'],
                pageTitle: 'noctemApp.recordPayment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-payment/record-payments.html',
                    controller: 'RecordPaymentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordPayment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('record-payment-detail', {
            parent: 'entity',
            url: '/record-payment/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_SUPERADMIN'],
                pageTitle: 'noctemApp.recordPayment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-payment/record-payment-detail.html',
                    controller: 'RecordPaymentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordPayment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RecordPayment', function($stateParams, RecordPayment) {
                    return RecordPayment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'record-payment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('record-payment-detail.edit', {
            parent: 'record-payment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_SUPERADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-payment/record-payment-dialog.html',
                    controller: 'RecordPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RecordPayment', function(RecordPayment) {
                            return RecordPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('record-payment.new', {
            parent: 'record-payment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_SUPERADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-payment/record-payment-dialog.html',
                    controller: 'RecordPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pay: null,
                                acceptPayment: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('record-payment', null, { reload: 'record-payment' });
                }, function() {
                    $state.go('record-payment');
                });
            }]
        })
        .state('record-payment.edit', {
            parent: 'record-payment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_SUPERADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-payment/record-payment-dialog.html',
                    controller: 'RecordPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RecordPayment', function(RecordPayment) {
                            return RecordPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('record-payment', null, { reload: 'record-payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('record-payment.delete', {
            parent: 'record-payment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_SUPERADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-payment/record-payment-delete-dialog.html',
                    controller: 'RecordPaymentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RecordPayment', function(RecordPayment) {
                            return RecordPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('record-payment', null, { reload: 'record-payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
