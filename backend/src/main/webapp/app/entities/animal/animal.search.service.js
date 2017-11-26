(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .factory('AnimalSearch', AnimalSearch);

    AnimalSearch.$inject = ['$resource'];

    function AnimalSearch($resource) {
        var resourceUrl =  'api/_search/animals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
