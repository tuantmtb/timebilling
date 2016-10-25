(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectHourDeleteController',ProjectHourDeleteController);

    ProjectHourDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectHour'];

    function ProjectHourDeleteController($uibModalInstance, entity, ProjectHour) {
        var vm = this;

        vm.projectHour = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectHour.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
