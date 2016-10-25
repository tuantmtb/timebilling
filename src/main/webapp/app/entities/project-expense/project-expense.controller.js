(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectExpenseController', ProjectExpenseController);

    ProjectExpenseController.$inject = ['$scope', '$state', 'ProjectExpense'];

    function ProjectExpenseController ($scope, $state, ProjectExpense) {
        var vm = this;
        
        vm.projectExpenses = [];

        loadAll();

        function loadAll() {
            ProjectExpense.query(function(result) {
                vm.projectExpenses = result;
            });
        }
    }
})();
