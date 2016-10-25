(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectHourDialogController', ProjectHourDialogController);

    ProjectHourDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectHour', 'Employee'];

    function ProjectHourDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectHour, Employee) {
        var vm = this;

        vm.projectHour = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectHour.id !== null) {
                ProjectHour.update(vm.projectHour, onSaveSuccess, onSaveError);
            } else {
                ProjectHour.save(vm.projectHour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('timebillingApp:projectHourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateWorked = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
