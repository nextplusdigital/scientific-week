(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vacinacao', {
            parent: 'entity',
            url: '/vacinacao?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.vacinacao.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacinacao/vacinacaos.html',
                    controller: 'VacinacaoController',
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
                    $translatePartialLoader.addPart('vacinacao');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vacinacao-detail', {
            parent: 'vacinacao',
            url: '/vacinacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.vacinacao.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacinacao/vacinacao-detail.html',
                    controller: 'VacinacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vacinacao');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Vacinacao', function($stateParams, Vacinacao) {
                    return Vacinacao.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vacinacao',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vacinacao-detail.edit', {
            parent: 'vacinacao-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao/vacinacao-dialog.html',
                    controller: 'VacinacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vacinacao', function(Vacinacao) {
                            return Vacinacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacinacao.new', {
            parent: 'vacinacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao/vacinacao-dialog.html',
                    controller: 'VacinacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                data: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vacinacao', null, { reload: 'vacinacao' });
                }, function() {
                    $state.go('vacinacao');
                });
            }]
        })
        .state('vacinacao.edit', {
            parent: 'vacinacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao/vacinacao-dialog.html',
                    controller: 'VacinacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vacinacao', function(Vacinacao) {
                            return Vacinacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacinacao', null, { reload: 'vacinacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacinacao.delete', {
            parent: 'vacinacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacinacao/vacinacao-delete-dialog.html',
                    controller: 'VacinacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vacinacao', function(Vacinacao) {
                            return Vacinacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacinacao', null, { reload: 'vacinacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
