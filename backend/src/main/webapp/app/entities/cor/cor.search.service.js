(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .factory('CorSearch', CorSearch);

    CorSearch.$inject = ['$resource'];

    function CorSearch($resource) {
        var resourceUrl =  'api/_search/cors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
