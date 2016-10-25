(function() {
    'use strict';
    angular
        .module('timebillingApp')
        .factory('ProjectPayment', ProjectPayment);

    ProjectPayment.$inject = ['$resource', 'DateUtils'];

    function ProjectPayment ($resource, DateUtils) {
        var resourceUrl =  'api/project-payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
