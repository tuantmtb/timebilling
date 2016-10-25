(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectHourDetailController', ProjectHourDetailController);

    ProjectHourDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectHour', 'Employee'];

    function ProjectHourDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectHour, Employee) {
        var vm = this;

        vm.projectHour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('timebillingApp:projectHourUpdate', function(event, result) {
            vm.projectHour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
