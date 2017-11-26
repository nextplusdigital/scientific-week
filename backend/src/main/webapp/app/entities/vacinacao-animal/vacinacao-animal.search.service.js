(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .factory('VacinacaoAnimalSearch', VacinacaoAnimalSearch);

    VacinacaoAnimalSearch.$inject = ['$resource'];

    function VacinacaoAnimalSearch($resource) {
        var resourceUrl =  'api/_search/vacinacao-animals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
