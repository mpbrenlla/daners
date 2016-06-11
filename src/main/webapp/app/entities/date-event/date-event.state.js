(function() {
    'use strict';

    angular
        .module('danersApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('date-event', {
            parent: 'entity',
            url: '/date-event?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DateEvents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/date-event/date-events.html',
                    controller: 'DateEventController',
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
                }]
            }
        })
        .state('date-event-detail', {
            parent: 'entity',
            url: '/date-event/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DateEvent'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/date-event/date-event-detail.html',
                    controller: 'DateEventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DateEvent', function($stateParams, DateEvent) {
                    return DateEvent.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('date-event.new', {
            parent: 'date-event',
            url: '/new/{date}',
            data: {
                authorities: ['ROLE_USER']
            },
            
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/date-event/date-event-dialog.html',
                    controller: 'DateEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            console.log("State");
                            console.log($stateParams.date);
                            return {
                                date: new Date($stateParams.date),
                                breakfast: null,
                                brunch: null,
                                lunch: null,
                                tea: null,
                                dinner: null,
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }]
        })
        .state('date-event.edit', {
            parent: 'date-event',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/date-event/date-event-dialog.html',
                    controller: 'DateEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DateEvent', function(DateEvent) {
                            return DateEvent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('date-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('date-event.delete', {
            parent: 'date-event',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/date-event/date-event-delete-dialog.html',
                    controller: 'DateEventDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DateEvent', function(DateEvent) {
                            return DateEvent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('date-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
