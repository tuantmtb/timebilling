(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-hour', {
            parent: 'entity',
            url: '/project-hour',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProjectHours'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-hour/project-hours.html',
                    controller: 'ProjectHourController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('project-hour-detail', {
            parent: 'entity',
            url: '/project-hour/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProjectHour'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-hour/project-hour-detail.html',
                    controller: 'ProjectHourDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProjectHour', function($stateParams, ProjectHour) {
                    return ProjectHour.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-hour',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-hour-detail.edit', {
            parent: 'project-hour-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-hour/project-hour-dialog.html',
                    controller: 'ProjectHourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectHour', function(ProjectHour) {
                            return ProjectHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-hour.new', {
            parent: 'project-hour',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-hour/project-hour-dialog.html',
                    controller: 'ProjectHourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateWorked: null,
                                description: null,
                                billableHour: null,
                                workCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-hour', null, { reload: 'project-hour' });
                }, function() {
                    $state.go('project-hour');
                });
            }]
        })
        .state('project-hour.edit', {
            parent: 'project-hour',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-hour/project-hour-dialog.html',
                    controller: 'ProjectHourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectHour', function(ProjectHour) {
                            return ProjectHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-hour', null, { reload: 'project-hour' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-hour.delete', {
            parent: 'project-hour',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-hour/project-hour-delete-dialog.html',
                    controller: 'ProjectHourDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectHour', function(ProjectHour) {
                            return ProjectHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-hour', null, { reload: 'project-hour' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
