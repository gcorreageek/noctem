(function() {
    'use strict';

    angular
        .module('noctemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('card', {
            parent: 'entity',
            url: '/card',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'noctemApp.card.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/card/cards.html',
                    controller: 'CardController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('card');
                    $translatePartialLoader.addPart('typeCard');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('card-detail', {
            parent: 'entity',
            url: '/card/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'noctemApp.card.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/card/card-detail.html',
                    controller: 'CardDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('card');
                    $translatePartialLoader.addPart('typeCard');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Card', function($stateParams, Card) {
                    return Card.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'card',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('card-detail.edit', {
            parent: 'card-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card/card-dialog.html',
                    controller: 'CardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Card', function(Card) {
                            return Card.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('card.new', {
            parent: 'card',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card/card-dialog.html',
                    controller: 'CardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                typeCard: null,
                                number: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('card', null, { reload: 'card' });
                }, function() {
                    $state.go('card');
                });
            }]
        })
        .state('card.edit', {
            parent: 'card',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card/card-dialog.html',
                    controller: 'CardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Card', function(Card) {
                            return Card.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('card', null, { reload: 'card' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('card.delete', {
            parent: 'card',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card/card-delete-dialog.html',
                    controller: 'CardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Card', function(Card) {
                            return Card.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('card', null, { reload: 'card' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
