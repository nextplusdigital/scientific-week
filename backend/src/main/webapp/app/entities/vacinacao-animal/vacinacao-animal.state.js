(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vacinacao-animal', {
            parent: 'entity',
            url: '/vacinacao-animal?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.vacinacaoAnimal.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacinacao-animal/vacinacao-animals.html',
                    controller: 'VacinacaoAnimalController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vacinacaoAnimal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vacinacao-animal-detail', {
            parent: 'vacinacao-animal',
            url: '/vacinacao-animal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.vacinacaoAnimal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacinacao-animal/vacinacao-animal-detail.html',
                    controller: 'VacinacaoAnimalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vacinacaoAnimal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VacinacaoAnimal', function($stateParams, VacinacaoAnimal) {
                    return VacinacaoAnimal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vacinacao-animal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vacinacao-animal-detail.edit', {
            parent: 'vacinacao-animal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao-animal/vacinacao-animal-dialog.html',
                    controller: 'VacinacaoAnimalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VacinacaoAnimal', function(VacinacaoAnimal) {
                            return VacinacaoAnimal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacinacao-animal.new', {
            parent: 'vacinacao-animal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao-animal/vacinacao-animal-dialog.html',
                    controller: 'VacinacaoAnimalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                identificacao: null,
                                quantidade: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vacinacao-animal', null, { reload: 'vacinacao-animal' });
                }, function() {
                    $state.go('vacinacao-animal');
                });
            }]
        })
        .state('vacinacao-animal.edit', {
            parent: 'vacinacao-animal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao-animal/vacinacao-animal-dialog.html',
                    controller: 'VacinacaoAnimalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VacinacaoAnimal', function(VacinacaoAnimal) {
                            return VacinacaoAnimal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacinacao-animal', null, { reload: 'vacinacao-animal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacinacao-animal.delete', {
            parent: 'vacinacao-animal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao-animal/vacinacao-animal-delete-dialog.html',
                    controller: 'VacinacaoAnimalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VacinacaoAnimal', function(VacinacaoAnimal) {
                            return VacinacaoAnimal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacinacao-animal', null, { reload: 'vacinacao-animal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
