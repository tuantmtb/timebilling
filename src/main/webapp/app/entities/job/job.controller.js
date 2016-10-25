(function() {
    'use strict';

    angular
        .module('timebillingApp')
        .controller('JobController', JobController);

    JobController.$inject = ['$scope', '$state', 'Job'];

    function JobController ($scope, $state, Job) {
        var vm = this;
        
        vm.jobs = [];

        loadAll();

        function loadAll() {
            Job.query(function(result) {
                vm.jobs = result;
            });
        }
    }
})();
