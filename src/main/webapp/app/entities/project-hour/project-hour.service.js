(function() {
    'use strict';
    angular
        .module('timebillingApp')
        .factory('ProjectHour', ProjectHour);

    ProjectHour.$inject = ['$resource', 'DateUtils'];

    function ProjectHour ($resource, DateUtils) {
        var resourceUrl =  'api/project-hours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateWorked = DateUtils.convertLocalDateFromServer(data.dateWorked);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateWorked = DateUtils.convertLocalDateToServer(copy.dateWorked);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateWorked = DateUtils.convertLocalDateToServer(copy.dateWorked);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
