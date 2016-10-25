(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectExpenseDialogController', ProjectExpenseDialogController);

    ProjectExpenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectExpense', 'Project', 'Employee'];

    function ProjectExpenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectExpense, Project, Employee) {
        var vm = this;

        vm.projectExpense = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.projects = Project.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectExpense.id !== null) {
                ProjectExpense.update(vm.projectExpense, onSaveSuccess, onSaveError);
            } else {
                ProjectExpense.save(vm.projectExpense, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('timebillingApp:projectExpenseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
