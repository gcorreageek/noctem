(function() {
    'use strict';

    angular
        .module('noctemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('record-item', {
            parent: 'entity',
            url: '/record-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'noctemApp.recordItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-item/record-items.html',
                    controller: 'RecordItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('record-item-detail', {
            parent: 'entity',
            url: '/record-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'noctemApp.recordItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-item/record-item-detail.html',
                    controller: 'RecordItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RecordItem', function($stateParams, RecordItem) {
                    return RecordItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'record-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('record-item-detail.edit', {
            parent: 'record-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-item/record-item-dialog.html',
                    controller: 'RecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RecordItem', function(RecordItem) {
                            return RecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('record-item.new', {
            parent: 'record-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-item/record-item-dialog.html',
                    controller: 'RecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                quantity: null,
                                price: null,
                                total: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('record-item', null, { reload: 'record-item' });
                }, function() {
                    $state.go('record-item');
                });
            }]
        })
        .state('record-item.edit', {
            parent: 'record-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-item/record-item-dialog.html',
                    controller: 'RecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RecordItem', function(RecordItem) {
                            return RecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('record-item', null, { reload: 'record-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('record-item.delete', {
            parent: 'record-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-item/record-item-delete-dialog.html',
                    controller: 'RecordItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RecordItem', function(RecordItem) {
                            return RecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('record-item', null, { reload: 'record-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
