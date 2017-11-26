(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .factory('RemedioSearch', RemedioSearch);

    RemedioSearch.$inject = ['$resource'];

    function RemedioSearch($resource) {
        var resourceUrl =  'api/_search/remedios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
