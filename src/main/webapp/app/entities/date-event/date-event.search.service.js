(function() {
    'use strict';

    angular
        .module('danersApp')
        .factory('DateEventSearch', DateEventSearch);

    DateEventSearch.$inject = ['$resource'];

    function DateEventSearch($resource) {
        var resourceUrl =  'api/_search/date-events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
