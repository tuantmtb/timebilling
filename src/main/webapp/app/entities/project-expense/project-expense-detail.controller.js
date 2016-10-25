(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectExpenseDetailController', ProjectExpenseDetailController);

    ProjectExpenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectExpense', 'Project', 'Employee'];

    function ProjectExpenseDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectExpense, Project, Employee) {
        var vm = this;

        vm.projectExpense = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('timebillingApp:projectExpenseUpdate', function(event, result) {
            vm.projectExpense = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
