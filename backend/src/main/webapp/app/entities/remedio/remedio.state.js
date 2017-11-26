(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('remedio', {
            parent: 'entity',
            url: '/remedio?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.remedio.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/remedio/remedios.html',
                    controller: 'RemedioController',
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
                    $translatePartialLoader.addPart('remedio');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('remedio-detail', {
            parent: 'remedio',
            url: '/remedio/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'romaninisistemasApp.remedio.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/remedio/remedio-detail.html',
                    controller: 'RemedioDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('remedio');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Remedio', function($stateParams, Remedio) {
                    return Remedio.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'remedio',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('remedio-detail.edit', {
            parent: 'remedio-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remedio/remedio-dialog.html',
                    controller: 'RemedioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Remedio', function(Remedio) {
                            return Remedio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('remedio.new', {
            parent: 'remedio',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remedio/remedio-dialog.html',
                    controller: 'RemedioDialogController',
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
                    $state.go('remedio', null, { reload: 'remedio' });
                }, function() {
                    $state.go('remedio');
                });
            }]
        })
        .state('remedio.edit', {
            parent: 'remedio',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remedio/remedio-dialog.html',
                    controller: 'RemedioDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Remedio', function(Remedio) {
                            return Remedio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('remedio', null, { reload: 'remedio' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('remedio.delete', {
            parent: 'remedio',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/remedio/remedio-delete-dialog.html',
                    controller: 'RemedioDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Remedio', function(Remedio) {
                            return Remedio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('remedio', null, { reload: 'remedio' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
