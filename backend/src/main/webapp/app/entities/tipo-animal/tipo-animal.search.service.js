(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .factory('TipoAnimalSearch', TipoAnimalSearch);

    TipoAnimalSearch.$inject = ['$resource'];

    function TipoAnimalSearch($resource) {
        var resourceUrl =  'api/_search/tipo-animals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
