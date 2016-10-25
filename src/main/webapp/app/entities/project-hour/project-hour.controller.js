(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectHourController', ProjectHourController);

    ProjectHourController.$inject = ['$scope', '$state', 'ProjectHour'];

    function ProjectHourController ($scope, $state, ProjectHour) {
        var vm = this;
        
        vm.projectHours = [];

        loadAll();

        function loadAll() {
            ProjectHour.query(function(result) {
                vm.projectHours = result;
            });
        }
    }
})();
