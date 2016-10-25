(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectExpenseDeleteController',ProjectExpenseDeleteController);

    ProjectExpenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectExpense'];

    function ProjectExpenseDeleteController($uibModalInstance, entity, ProjectExpense) {
        var vm = this;

        vm.projectExpense = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectExpense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
