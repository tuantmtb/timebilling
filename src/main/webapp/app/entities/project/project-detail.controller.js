(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'Customer', 'Employee', 'Category'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, Customer, Employee, Category) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('timebillingApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
