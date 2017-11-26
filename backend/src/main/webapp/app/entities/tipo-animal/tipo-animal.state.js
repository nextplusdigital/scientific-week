(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tipo-animal', {
            parent: 'entity',
            url: '/tipo-animal?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.tipoAnimal.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-animal/tipo-animals.html',
                    controller: 'TipoAnimalController',
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
                    $translatePartialLoader.addPart('tipoAnimal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tipo-animal-detail', {
            parent: 'tipo-animal',
            url: '/tipo-animal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.tipoAnimal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-animal/tipo-animal-detail.html',
                    controller: 'TipoAnimalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tipoAnimal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TipoAnimal', function($stateParams, TipoAnimal) {
                    return TipoAnimal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tipo-animal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tipo-animal-detail.edit', {
            parent: 'tipo-animal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-animal/tipo-animal-dialog.html',
                    controller: 'TipoAnimalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TipoAnimal', function(TipoAnimal) {
                            return TipoAnimal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipo-animal.new', {
            parent: 'tipo-animal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-animal/tipo-animal-dialog.html',
                    controller: 'TipoAnimalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tipo-animal', null, { reload: 'tipo-animal' });
                }, function() {
                    $state.go('tipo-animal');
                });
            }]
        })
        .state('tipo-animal.edit', {
            parent: 'tipo-animal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-animal/tipo-animal-dialog.html',
                    controller: 'TipoAnimalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TipoAnimal', function(TipoAnimal) {
                            return TipoAnimal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-animal', null, { reload: 'tipo-animal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipo-animal.delete', {
            parent: 'tipo-animal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-animal/tipo-animal-delete-dialog.html',
                    controller: 'TipoAnimalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TipoAnimal', function(TipoAnimal) {
                            return TipoAnimal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-animal', null, { reload: 'tipo-animal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
