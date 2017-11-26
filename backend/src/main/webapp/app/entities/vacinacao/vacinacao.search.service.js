(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .factory('VacinacaoSearch', VacinacaoSearch);

    VacinacaoSearch.$inject = ['$resource'];

    function VacinacaoSearch($resource) {
        var resourceUrl =  'api/_search/vacinacaos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
