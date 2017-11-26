(function() {
    'use strict';
    angular
        .module('romaninisistemasApp')
        .factory('VacinacaoAnimal', VacinacaoAnimal);

    VacinacaoAnimal.$inject = ['$resource'];

    function VacinacaoAnimal ($resource) {
        var resourceUrl =  'api/vacinacao-animals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
