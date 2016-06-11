(function() {
    'use strict';
    angular
        .module('danersApp')
        .factory('DateEvent', DateEvent);

    DateEvent.$inject = ['$resource', 'DateUtils'];

    function DateEvent ($resource, DateUtils) {
        var resourceUrl =  'api/date-events/:id';

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
