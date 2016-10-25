(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-expense', {
            parent: 'entity',
            url: '/project-expense',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProjectExpenses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-expense/project-expenses.html',
                    controller: 'ProjectExpenseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('project-expense-detail', {
            parent: 'entity',
            url: '/project-expense/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProjectExpense'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-expense/project-expense-detail.html',
                    controller: 'ProjectExpenseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProjectExpense', function($stateParams, ProjectExpense) {
                    return ProjectExpense.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-expense',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-expense-detail.edit', {
            parent: 'project-expense-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-expense/project-expense-dialog.html',
                    controller: 'ProjectExpenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectExpense', function(ProjectExpense) {
                            return ProjectExpense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-expense.new', {
            parent: 'project-expense',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-expense/project-expense-dialog.html',
                    controller: 'ProjectExpenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                description: null,
                                code: null,
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-expense', null, { reload: 'project-expense' });
                }, function() {
                    $state.go('project-expense');
                });
            }]
        })
        .state('project-expense.edit', {
            parent: 'project-expense',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-expense/project-expense-dialog.html',
                    controller: 'ProjectExpenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectExpense', function(ProjectExpense) {
                            return ProjectExpense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-expense', null, { reload: 'project-expense' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-expense.delete', {
            parent: 'project-expense',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-expense/project-expense-delete-dialog.html',
                    controller: 'ProjectExpenseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectExpense', function(ProjectExpense) {
                            return ProjectExpense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-expense', null, { reload: 'project-expense' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
